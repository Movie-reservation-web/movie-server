package study.movie.domain.theater;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.payment.AgeType;
import study.movie.global.enumMapper.EnumMapperType;

import java.util.Map;
import java.util.Set;

import static study.movie.domain.payment.AgeType.ADULT;
import static study.movie.domain.payment.AgeType.TEENAGER;

/**
 * FilmFormat 속성 안쓰는 경우 삭제!
 */
@AllArgsConstructor
@Getter
public enum ScreenFormat implements EnumMapperType {
    TWO_D(
            "2D",
            Set.of(FilmFormat.TWO_D),
            Map.of(ADULT, 14000, TEENAGER, 11000),
            false
    ),
    IMAX(
            "IMAX",
            Set.of(FilmFormat.IMAX),
            Map.of(ADULT, 21000, TEENAGER, 17000),
            false
    ),
    FOUR_D_FLEX(
            "4DX",
            Set.of(FilmFormat.FOUR_D_FLEX),
            Map.of(ADULT, 19000, TEENAGER, 13000),
            false
    ),
    SCREEN_X(
            "ScreenX",
            Set.of(FilmFormat.SCREEN_X),
            Map.of(ADULT, 16000, TEENAGER, 13000),
            false
    ),
    FOUR_D_FLEX_SCREEN(
            "4DX SCREEN",
            Set.of(FilmFormat.FOUR_D_FLEX, FilmFormat.SCREEN_X),
            Map.of(ADULT, 21000, TEENAGER, 15000),
            false
    ),
    PREMIUM(
            "PREMIUM",
            Set.of(FilmFormat.TWO_D),
            Map.of(ADULT, 25000, TEENAGER, 20000),
            true
    ),
    CINE_DE_CHEF(
            "CINE de CHEF",
            Set.of(FilmFormat.TWO_D),
            Map.of(ADULT, 45000, TEENAGER, 35000),
            true
    ),
    SKY_BOX(
            "SKY BOX",
            Set.of(FilmFormat.TWO_D),
            Map.of(ADULT, 50000, TEENAGER, 50000),
            true
    );
    private final String value;
    private final Set<FilmFormat> filmFormats;
    private final Map<AgeType, Integer> ageCriterionMap;
    private final boolean isFixRate;

    @Override
    public String getCode() {
        return name();
    }

    public int getPrice(AgeType ageType) {
        return ageCriterionMap.get(ageType);
    }

}