package study.movie.converter.movie;

import study.movie.global.constants.EntityAttrConst.MovieGenre;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class MovieGenreConverter extends EnumArrayConverter<MovieGenre> {
    public MovieGenreConverter() {
        super(MovieGenre.class);
    }
}
