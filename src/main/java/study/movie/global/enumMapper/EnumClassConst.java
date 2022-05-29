package study.movie.global.enumMapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EnumClassConst implements EnumMapperType {
    // Member
    GENDER_TYPE("GenderType", "성별"),

    // Movie
    FILM_FORMAT("FilmFormat", "영화 포멧"),
    FILM_RATING("FilmRating", "상영 등급"),
    MOVIE_GENRE("MovieGenre", "장르"),
    MOVIE_SEARCH_TYPE("MovieSearchCondType", "영화 정렬 방식"),
    MOVIE_CHART_SORT_TYPE("MovieChartSortType", "영화차트 정렬 방식"),
    MOVIE_SORT_TYPE("MovieSortType", "영화 정렬 방식"),
    REVIEW_SEARCH_TYPE("ReviewSearchCondType", "리뷰 검색 타입"),
    REVIEW_SORT_TYPE("ReviewSortType", "리뷰 정렬 방식"),

    // Theater
    CITY_CODE("CityCode", "도시"),
    SCREEN_FORMAT("ScreenFormat", "상영관 포멧"),
    THEATER_SEARCH_TYPE("TheaterSearchType", "영화관 검색 타입"),
    THEATER_SORT_TYPE("TheaterSortType", "영화관 정렬 방식"),
    SCREEN_SEARCH_TYPE("ScreenSearchType", "상영관 검색 타입"),
    SCREEN_SORT_TYPE("ScreenSortType", "상영관 정렬 방식"),

    // Schedule
    SCHEDULE_STATUS("ScheduleStatus", "스케줄 상태"),
    SCHEDULE_SEARCH_TYPE("ScheduleSearchCondType", "상영일정 검색 타입"),
    SCHEDULE_SORT_TYPE("ScheduleSortType", "상영일정 정렬 방식"),

    // Ticket
    TICKET_STATUS("TicketStatus", "예매 상태"),
    TICKET_SEARCH_TYPE("TicketSearchCondType", "티켓 검색 타입"),
    TICKET_SORT_TYPE("TicketSortType", "티켓 정렬 방식");
    private String className;
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
