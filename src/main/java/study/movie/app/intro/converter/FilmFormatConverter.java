package study.movie.app.intro.converter;

import study.movie.global.constants.EntityAttrConst.FilmFormat;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class FilmFormatConverter extends EnumArrayConverter<FilmFormat> {
    public FilmFormatConverter() {
        super(FilmFormat.class);
    }
}
