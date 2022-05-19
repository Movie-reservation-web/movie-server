package study.movie.movie.converter;

import study.movie.movie.entity.MovieGenre;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class MovieGenreConverter extends EnumArrayConverter<MovieGenre> {
    public MovieGenreConverter() {
        super(MovieGenre.class);
    }
}
