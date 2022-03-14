package study.movie.converter.movie;

import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

import static study.movie.global.constants.EntityAttrConst.MovieGenre;

@Converter(autoApply = true)
public class GenreConverter extends CodeValueConverter<MovieGenre> {
    public GenreConverter() {super(MovieGenre.class);}
}
