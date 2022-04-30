package study.movie.global.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostIdResponse{
    private Long id;

    public static PostIdResponse of(Long id) {
        return PostIdResponse.builder().id(id).build();
    }
}
