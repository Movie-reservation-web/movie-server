package study.movie.app.intro.converter;

import study.movie.global.constants.EntityAttrConst;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

import static study.movie.global.constants.EntityAttrConst.*;
import static study.movie.global.constants.EntityAttrConst.Genre;

@Converter(autoApply = true)
public class FilmRatingConverter extends CodeValueConverter<FilmRating> {
    public FilmRatingConverter() {super(FilmRating.class);}
}
