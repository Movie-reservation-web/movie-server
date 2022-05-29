package study.movie.domain.theater.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateTheaterRequest {

    @NotNull
    private String phone;
}
