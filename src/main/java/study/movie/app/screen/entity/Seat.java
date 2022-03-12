package study.movie.app.screen.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import java.util.ArrayList;
import java.util.List;

import static study.movie.global.constants.EntityAttrConst.ReserveStatus;

@Embeddable
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat{

    private Integer row;
    private Integer column;

    @Enumerated(EnumType.STRING)
    private ReserveStatus status;

}
