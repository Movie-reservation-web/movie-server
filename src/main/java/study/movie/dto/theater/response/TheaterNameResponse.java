package study.movie.dto.theater.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.movie.domain.theater.Theater;

@Data
public class TheaterNameResponse {

    private Long theaterId;
    private String theaterName;

    public TheaterNameResponse(Theater theater){
        this.theaterId = theater.getId();
        this.theaterName = theater.getName();
    }
}
