package study.movie.domain.screen;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static study.movie.global.constants.EntityAttrConst.SeatStatus;

@Embeddable
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat{

    private Integer row;
    private Integer column;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

}
