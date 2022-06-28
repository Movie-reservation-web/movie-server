package study.movie;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import study.movie.global.config.OAuthProperties;
import study.movie.global.config.IamportProperties;
import study.movie.global.config.RedisProperties;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({OAuthProperties.class, IamportProperties.class})
public class MovieApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieApplication.class, args);
    }

    @Bean
    public JPAQueryFactory queryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
