package study.movie.theater.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateTheaterRequest {

    @NotNull
    private String phone;
}
