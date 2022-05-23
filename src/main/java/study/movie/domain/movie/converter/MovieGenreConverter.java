package study.movie.domain.movie.converter;

import study.movie.domain.movie.entity.MovieGenre;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class MovieGenreConverter extends EnumArrayConverter<MovieGenre> {
    public MovieGenreConverter() {
        super(MovieGenre.class);
    }
}
