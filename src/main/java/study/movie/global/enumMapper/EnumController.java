package study.movie.global.enumMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.global.dto.CustomResponse;

import static study.movie.global.enumMapper.EnumClassConst.*;
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
     * 엔티티 상태 코드
     */
    @GetMapping("/status/{entityName}")
    public ResponseEntity<?> getEntityStatus(@PathVariable String entityName) {
        EnumClassConst eStatus = getEnumClassConst(entityName, EnumMapper.STATUS_PATH);
        return getCategories(eStatus);
    }

    /**
     * 엔티티 검색 방식 타입
     */
    @GetMapping("/search-conditions/{entityName}")
    public ResponseEntity<?> getEntitySearchCondTypes(@PathVariable String entityName) {
        EnumClassConst eSearchCondType = getEnumClassConst(entityName, EnumMapper.SEARCH_COND_TYPE_PATH);
        return getCategories(eSearchCondType);
    }

    /**
     * 엔티티 검색 정렬 타입
     */
    @GetMapping("/sort-types/{entityName}")
    public ResponseEntity<?> getEntitySortTypes(@PathVariable String entityName) {
        EnumClassConst eSortType = getEnumClassConst(entityName, EnumMapper.SORT_TYPE_PATH);
        return getCategories(eSortType);
    }

    /**
     * EnumClassConst 에 있는 카테고리 조회
     *
     * @param category
     */
    private ResponseEntity<?> getCategories(EnumClassConst category) {
        return CustomResponse.success(category.getValue() + READ_CATEGORY, enumMapper.get(category.getClassName()));
    }

    private EnumClassConst getEnumClassConst(String entityName, String path) {
        return EnumClassConst.valueOf(entityName.toUpperCase() + path);
    }
}
