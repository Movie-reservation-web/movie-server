package study.movie.dto.theater.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import study.movie.domain.schedule.Schedule;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.theater.Theater;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Data
@NotNull
public class CreateScreenRequest {

    private String name;
    private ScreenFormat format;
    private Integer maxRows;
    private Integer maxCols;
    private Long theaterId;

}
