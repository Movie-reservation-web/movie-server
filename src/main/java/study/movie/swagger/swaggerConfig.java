package study.movie.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class swaggerConfig {

    @Bean
    public Docket SwaggerApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerInfo()) // API Doc 및 작성자 정보 매핑
                .select()
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false); // 기본 세팅값은 200, 401, 402, 403, 404 사용X
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("I-MOVIE API Documentation")
                .description("I-MOVIE 서버 API 설명을 위한 문서입니다.")
                .license("imovie")
                .version("1")
                .build();
    }
}
