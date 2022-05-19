package study.movie.theater.dto.response;

import lombok.Data;
import study.movie.theater.entity.Theater;

@Data
public class TheaterNameResponse {

    private Long theaterId;
    private String theaterName;

    public TheaterNameResponse(Theater theater){
        this.theaterId = theater.getId();
        this.theaterName = theater.getName();
    }
}
