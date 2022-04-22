package study.movie.global.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import java.util.Locale;

@Data
@NoArgsConstructor
public class ValidationResponse {

    private String target;
    private Object rejectValue;
    private String detail;

    public ValidationResponse(FieldError fieldError, MessageSource messageSource) {
        this.target = fieldError.getField();
        this.rejectValue = fieldError.getRejectedValue();
        this.detail = messageSource.getMessage(fieldError, Locale.KOREA);
    }
}
