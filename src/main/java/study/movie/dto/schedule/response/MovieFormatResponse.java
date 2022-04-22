package study.movie.dto.schedule.response;

import lombok.Data;
import study.movie.domain.theater.ScreenFormat;

import java.util.List;

@Data
public class MovieFormatResponse {

    private String movieTitle;
    private List<ScreenFormat> formats;

    public MovieFormatResponse(String movieTitle, List<ScreenFormat> formats) {
        this.movieTitle = movieTitle;
        this.formats = formats;
    }
}
