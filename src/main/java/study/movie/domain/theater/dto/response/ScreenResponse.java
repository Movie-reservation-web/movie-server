package study.movie.domain.theater.dto.response;

import lombok.Builder;
import lombok.Getter;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.ScreenFormat;

@Getter
@Builder
public class ScreenResponse {

    private Long id;
    private String name;
    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;
    private Integer totalSeatCount;
    private String theaterName;

    public static ScreenResponse of(Screen screen) {
        return ScreenResponse.builder()
                .id(screen.getId())
                .name(screen.getName())
                .format(screen.getFormat())
                .maxRows(screen.getMaxRows())
                .maxCols(screen.getMaxCols())
                .totalSeatCount(screen.getTotalSeatCount())
                .theaterName(screen.getTheater().getName())
                .build();
    }
}
