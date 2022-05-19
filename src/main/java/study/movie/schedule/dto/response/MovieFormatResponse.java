package study.movie.schedule.dto.response;

import lombok.*;
import study.movie.theater.entity.ScreenFormat;

import java.util.List;

@Data
@Builder
public class MovieFormatResponse {

    private String movieTitle;
    private List<ScreenFormat> formats;

    public static MovieFormatResponse of(String movieTitle, List<ScreenFormat> formats) {
        return MovieFormatResponse.builder()
                .movieTitle(movieTitle)
                .formats(formats)
                .build();
    }
}
