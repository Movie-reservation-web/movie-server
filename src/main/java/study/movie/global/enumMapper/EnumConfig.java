package study.movie.global.enumMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.movie.member.entity.GenderType;
import study.movie.movie.dto.condition.*;
import study.movie.movie.entity.FilmFormat;
import study.movie.movie.entity.FilmRating;
import study.movie.movie.entity.MovieGenre;
import study.movie.schedule.dto.condition.ScheduleSearchType;
import study.movie.schedule.dto.condition.ScheduleSortType;
import study.movie.schedule.entity.ScheduleStatus;
import study.movie.theater.entity.CityCode;
import study.movie.theater.entity.ScreenFormat;
import study.movie.ticket.dto.condition.TicketSearchType;
import study.movie.ticket.dto.condition.TicketSortType;
import study.movie.ticket.entity.TicketStatus;

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
