package study.movie.domain.movie.converter;

import study.movie.domain.movie.entity.FilmFormat;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class FilmFormatConverter extends EnumArrayConverter<FilmFormat> {
    public FilmFormatConverter() {
        super(FilmFormat.class);
    }
}
