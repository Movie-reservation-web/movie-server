package study.movie.domain.theater.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateTheaterRequest {

    @NotBlank
    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}")
    private String phone;
}
