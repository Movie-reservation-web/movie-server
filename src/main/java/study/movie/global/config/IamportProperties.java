package study.movie.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iamport")
public class IamportProperties {
    private final Iamport iamport = new Iamport();

    @Getter
    @Setter
    public static class Iamport {
        private String key;
        private String secret;
    }

    public Iamport getIamport() {
        return iamport;
    }
}
