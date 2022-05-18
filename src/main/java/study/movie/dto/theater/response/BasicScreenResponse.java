package study.movie.dto.theater.response;

import lombok.Data;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;

@Data
public class BasicScreenResponse {

    private Long id;
    private String name;
    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;
    private Theater theater;

    public BasicScreenResponse(Screen screen){
        this.id = screen.getId();
        this.name = screen.getName();
        this.format = screen.getFormat();
        this.maxRows = screen.getMaxRows();
        this.maxCols = screen.getMaxCols();
        this.theater = screen.getTheater();
    }
}
