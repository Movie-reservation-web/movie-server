package study.movie.global.enumMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.global.constants.EnumClassConst;
import study.movie.global.dto.Response;

import static study.movie.global.constants.EnumClassConst.*;
import static study.movie.global.constants.ResponseMessage.READ_CATEGORY;

/**
 * {@link EnumClassConst}에 있는 Category 조희
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class EnumController {

    private final EnumMapper enumMapper;

    /**
     * 극장 도시
     */
    @GetMapping("/city")
    public ResponseEntity<?> getCities() {
        return getCategories(CITY_CODE);
    }

    /**
     * 영화 등급
     */
    @GetMapping("/film-rating")
    public ResponseEntity<?> getFilmRatings() {
        return getCategories(FILM_RATING);
    }

    /**
     * 영화 장르
     */
    @GetMapping("/movie-genre")
    public ResponseEntity<?> getMovieGenres() {
        return getCategories(MOVIE_GENRE);
    }

    /**
     * 상영관 종류
     */
    @GetMapping("/screen-format")
    public ResponseEntity<?> getScreenFormats() {
        return getCategories(SCREEN_FORMAT);
    }

    /**
     * 성별 타입
     */
    @GetMapping("/gender-type")
    public ResponseEntity<?> getGenderTypes() {
        return getCategories(GENDER_TYPE);
    }

    /**
     * 상영일정 검색 정렬 방식
     */
    @GetMapping("/sort-type/schedule")
    public ResponseEntity<?> getScheduleSortTypes() {
        return getCategories(SCHEDULE_SORT_TYPE);
    }

    /**
     * EnumClassConst 에 있는 카테고리 조회
     *
     * @param category
     */
    private ResponseEntity<?> getCategories(EnumClassConst category) {
        return Response.success(category.getValue() + READ_CATEGORY, enumMapper.get(category.getClassName()));

    }
}
