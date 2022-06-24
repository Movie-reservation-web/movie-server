package study.movie.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.movie.global.paging.PageableArgumentResolver;

import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("https://www.inf-cinema.shop","https://inf-cinema.shop","https://icinema.netlify.app/")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") //요청 허용 메서드
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageableArgumentResolver());
    }
}
