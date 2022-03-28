package study.movie.converter.movie;

import study.movie.domain.movie.FilmRating;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class FilmRatingConverter extends CodeValueConverter<FilmRating> {
    public FilmRatingConverter() {super(FilmRating.class);}
}
