package study.movie.movie.converter;

import study.movie.movie.entity.FilmFormat;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class FilmFormatConverter extends EnumArrayConverter<FilmFormat> {
    public FilmFormatConverter() {
        super(FilmFormat.class);
    }
}
