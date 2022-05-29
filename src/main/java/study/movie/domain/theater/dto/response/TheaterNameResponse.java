package study.movie.domain.theater.dto.response;

import lombok.Builder;
import lombok.Getter;
import study.movie.domain.theater.entity.Theater;

@Getter
@Builder
public class TheaterNameResponse {

    private Long id;
    private String name;

    public static TheaterNameResponse of(Theater theater) {
        return TheaterNameResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .build();
    }
}
