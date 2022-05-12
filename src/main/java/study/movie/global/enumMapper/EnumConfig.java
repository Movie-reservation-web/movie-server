package study.movie.global.enumMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.movie.domain.member.GenderType;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.MovieGenre;
import study.movie.domain.schedule.ScheduleStatus;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.ScreenFormat;
import study.movie.domain.ticket.TicketStatus;
import study.movie.dto.movie.condition.*;
import study.movie.dto.schedule.condition.ScheduleSearchType;
import study.movie.dto.schedule.condition.ScheduleSortType;
import study.movie.dto.ticket.condition.TicketSearchType;
import study.movie.dto.ticket.condition.TicketSortType;

import static study.movie.global.enumMapper.EnumClassConst.*;

@Configuration
public class EnumConfig {

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();

        // Member
        enumMapper.put(GENDER_TYPE, GenderType.class);

        // Movie
        enumMapper.put(FILM_FORMAT, FilmFormat.class);
        enumMapper.put(FILM_RATING, FilmRating.class);
        enumMapper.put(MOVIE_GENRE, MovieGenre.class);
        enumMapper.put(MOVIE_CHART_SORT_TYPE, MovieChartSortType.class);
        enumMapper.put(MOVIE_SORT_TYPE, MovieSortType.class);
        enumMapper.put(MOVIE_SEARCH_TYPE, MovieSearchType.class);
        enumMapper.put(REVIEW_SORT_TYPE, ReviewSortType.class);
        enumMapper.put(REVIEW_SEARCH_TYPE, ReviewSearchType.class);

        // Theater
        enumMapper.put(CITY_CODE, CityCode.class);
        enumMapper.put(SCREEN_FORMAT, ScreenFormat.class);

        // Schedule
        enumMapper.put(SCHEDULE_STATUS, ScheduleStatus.class);
        enumMapper.put(SCHEDULE_SEARCH_TYPE, ScheduleSearchType.class);
        enumMapper.put(SCHEDULE_SORT_TYPE, ScheduleSortType.class);

        // Ticket
        enumMapper.put(TICKET_STATUS, TicketStatus.class);
        enumMapper.put(TICKET_SEARCH_TYPE, TicketSearchType.class);
        enumMapper.put(TICKET_SORT_TYPE, TicketSortType.class);

        return enumMapper;
    }
}
