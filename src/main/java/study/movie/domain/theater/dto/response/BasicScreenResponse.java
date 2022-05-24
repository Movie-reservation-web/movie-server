package study.movie.domain.theater.dto.response;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Data;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.theater.entity.Theater;

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
