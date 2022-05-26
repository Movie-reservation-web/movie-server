package study.movie.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.movie.auth.CustomUserDetailsService;
import study.movie.auth.exception.CustomAccessDeniedHandler;
import study.movie.auth.exception.CustomAuthenticationEntryPoint;
import study.movie.auth.jwt.JwtAuthenticationFilter;
import study.movie.auth.jwt.JwtTokenProvider;
import study.movie.auth.oauth2.*;
import study.movie.auth.oauth2.CustomOAuth2Provider;
import study.movie.redis.RedisRepository;

import java.util.ArrayList;
import java.util.List;

import static study.movie.domain.member.entity.Role.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtProvider;
    private final RedisRepository redisRepository;
    private final CustomOauth2UserService customOauth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2AuthenticationProvider oAuth2AuthenticationProvider;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * authenticate 메소드로 인증 처리시 customUserDetailsServeice를 사용
     * passwordEncoder와 같이
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider, redisRepository);
    }

    @Bean
    public OAuth2AuthenticationFilter oAuth2AuthenticationFilter() {
        return new OAuth2AuthenticationFilter(oAuth2AuthenticationProvider, oAuth2SuccessHandler, oAuth2FailureHandler);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = new ArrayList<>();
        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .jwkSetUri("temp")
                .build()
        );
        return registrations;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(getRoleHierarchy());
        return roleHierarchy;
    }

    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                // 토큰을 활욜하면 세션이 필요 없어지므로 STATELESS 로 설정.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .expressionHandler(webExpressionHandler())
                        .antMatchers("/admin/**/create").hasRole(ADMIN.getCode())
                        .antMatchers("/admin/**").hasRole(MANAGER.getCode())
                        .antMatchers("/api/*/schedules/selected").hasRole(USER.getCode())
                        .antMatchers(
        //                        "/login/**",
                                "/api/*/auth/login",
                                "/api/*/members/sign-up",
                                "/api/*/movies/**",
                                "/api/*/schedules/**",
                                "/api/*/theater/**",
                                "/api/*/categories/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                .and()
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    .addFilterBefore(oAuth2AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOauth2UserService)
                .and()
                    .successHandler(oAuth2SuccessHandler)
                    .failureHandler(oAuth2FailureHandler);
    }
}
