package study.movie.app.intro.converter;

import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

import static study.movie.global.constants.EntityAttrConst.MovieGenre;

@Converter(autoApply = true)
public class GenreConverter extends CodeValueConverter<MovieGenre> {
    public GenreConverter() {super(MovieGenre.class);}
}
