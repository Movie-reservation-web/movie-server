package study.movie.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class SwaggerConfig {

    /**
     * Optional
     * 기본 정보에 관한 정보들을 customizing 가능
     */
    private static final Contact DEFAULT_CONTACT = new Contact(
            "Imovie",
            "https://github.com/Movie-reservation-web",
            "rbdus7174@gmail.com");

    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "Imovie API",
            "Imovie Project REST API service",
            "1.0",
            "urn:tos",
            DEFAULT_CONTACT,
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0",
            Collections.emptyList());

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(
            Arrays.asList("application/json", "application/xml")
    );


    /**
     * Docket -> 가지고 있는 문서의 내용을 문서화 시켜준다
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }
}
