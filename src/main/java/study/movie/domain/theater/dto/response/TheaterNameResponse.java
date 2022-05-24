package study.movie.domain.theater.dto.response;

import lombok.Data;
import study.movie.domain.theater.entity.Theater;
@Data
public class TheaterNameResponse {

    private Long theaterId;
    private String theaterName;

    public TheaterNameResponse(Theater theater){
        this.theaterId = theater.getId();
        this.theaterName = theater.getName();
    }
}
