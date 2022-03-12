package study.movie.app.intro.converter;

import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

import static study.movie.global.constants.EntityAttrConst.Genre;

@Converter(autoApply = true)
public class GenreConverter extends CodeValueConverter<Genre> {
    public GenreConverter() {super(Genre.class);}
}
