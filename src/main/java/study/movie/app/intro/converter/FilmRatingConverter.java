package study.movie.app.intro.converter;

import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

import static study.movie.global.constants.EntityAttrConst.*;

@Converter(autoApply = true)
public class FilmRatingConverter extends CodeValueConverter<FilmRating> {
    public FilmRatingConverter() {super(FilmRating.class);}
}
