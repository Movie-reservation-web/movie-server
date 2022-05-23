package study.movie.domain.schedule.dto.response;

import lombok.*;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.ScreenFormat;

@Data
@Builder
public class ScreenResponse {

    private Long id;
    private String name;
    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;
    private TheaterResponse theater;

    public static ScreenResponse of(Screen screen) {
        return ScreenResponse.builder()
                .id(screen.getId())
                .name(screen.getName())
                .format(screen.getFormat())
                .maxRows(screen.getMaxRows())
                .maxCols(screen.getMaxCols())
                .theater(TheaterResponse.of(screen.getTheater()))
                .build();
    }
}
