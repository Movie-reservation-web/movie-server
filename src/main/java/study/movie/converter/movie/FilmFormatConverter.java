package study.movie.converter.movie;

import study.movie.domain.movie.FilmFormat;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class FilmFormatConverter extends EnumArrayConverter<FilmFormat> {
    public FilmFormatConverter() {
        super(FilmFormat.class);
    }
}
