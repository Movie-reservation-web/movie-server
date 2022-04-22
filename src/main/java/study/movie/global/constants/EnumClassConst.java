package study.movie.global.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumClassConst {

    /**
     * Enum Class Name
     */
    FILM_RATING("FilmRating", "상영 등급"),
    FILM_FORMAT("FilmFormat", "영화 포멧"),
    MOVIE_GENRE("MovieGenre", "장르"),
    CITY_CODE("CityCode", "도시"),
    SCREEN_FORMAT("ScreenFormat", "상영관 포멧"),
    GENDER_TYPE("GenderType", "성별");
    private String className;
    private String value;


}
