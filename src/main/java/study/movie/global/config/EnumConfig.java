package study.movie.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.movie.domain.member.GenderType;
import study.movie.domain.movie.FilmFormat;
import study.movie.domain.movie.FilmRating;
import study.movie.domain.movie.MovieGenre;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.ScreenFormat;
import study.movie.global.enumMapper.EnumMapper;

import static study.movie.global.constants.EnumClassConstants.*;

@Configuration
public class EnumConfig {

    @Bean
    public EnumMapper enumMapper(){
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put(FILM_RATING, FilmRating.class);
        enumMapper.put(FILM_FORMAT, FilmFormat.class);
        enumMapper.put(MOVIE_GENRE, MovieGenre.class);
        enumMapper.put(CITY_CODE, CityCode.class);
        enumMapper.put(SCREEN_FORMAT, ScreenFormat.class);
        enumMapper.put(GENDER_TYPE, GenderType.class);
        return enumMapper;
    }
}
