package study.movie.movie.converter;

import study.movie.movie.entity.FilmRating;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class FilmRatingConverter extends CodeValueConverter<FilmRating> {
    public FilmRatingConverter() {super(FilmRating.class);}
}
