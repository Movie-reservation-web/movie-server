package study.movie.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.movie.auth.oauth2.CustomOauth2UserService;
import study.movie.auth.CustomUserDetailsService;
import study.movie.auth.oauth2.OAuth2SuccessHandler;
import study.movie.auth.exception.CustomAccessDeniedHandler;
import study.movie.auth.exception.CustomAuthenticationEntryPoint;
import study.movie.auth.jwt.JwtAuthenticationFilter;
import study.movie.auth.jwt.JwtTokenProvider;
import study.movie.redis.RedisRepository;

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
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

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

//    @Bean
//    public AccessDecisionManager accessDecisionManager() {
//        // role 계층 구조 설정
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        System.out.println(getRoleHierarchy());
//        roleHierarchy.setHierarchy(getRoleHierarchy());
//
//        // 핸들러 설정
//        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
//        handler.setRoleHierarchy(roleHierarchy);
//
//        // voter 설정
//        WebExpressionVoter voter = new WebExpressionVoter();
//        voter.setExpressionHandler(handler);
//
//        // voter 리스트 설정
//        List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList();
//
//        // AccessDecisionManager 설정
//        AccessDecisionManager accessDecisionManager = new AffirmativeBased(voters);
//
//        return accessDecisionManager;
//    }

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
                    .antMatchers("/admin/**/create").hasRole(ADMIN.getCode())
                    .antMatchers("/admin/**").hasRole(MANAGER.getCode())
                    .antMatchers("/api/*/schedules/selected").hasRole(USER.getCode())
                    .antMatchers(
                            "/api/*/auth/login",
                            "/api/*/members/sign-up",
                            "/api/*/movies/**",
                            "/api/*/schedules/**",
                            "/api/*/theater/**",
                            "/api/*/categories/**"
                    ).permitAll()
                    .anyRequest().authenticated()
//                    .accessDecisionManager(accessDecisionManager())
            .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
            .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, redisRepository), UsernamePasswordAuthenticationFilter.class)
            .oauth2Login()
                .userInfoEndpoint()
                .userService(customOauth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler);
    }
}
