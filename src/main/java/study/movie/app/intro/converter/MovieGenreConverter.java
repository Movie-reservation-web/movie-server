package study.movie.app.intro.converter;

import study.movie.global.constants.EntityAttrConst;
import study.movie.global.constants.EntityAttrConst.FilmFormat;
import study.movie.global.constants.EntityAttrConst.MovieGenre;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class MovieGenreConverter extends EnumArrayConverter<MovieGenre> {
    public MovieGenreConverter() {
        super(MovieGenre.class);
    }
}
