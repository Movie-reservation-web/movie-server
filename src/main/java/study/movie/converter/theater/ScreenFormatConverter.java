package study.movie.converter.theater;

import study.movie.domain.theater.ScreenFormat;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class ScreenFormatConverter extends EnumArrayConverter<ScreenFormat> {
    public ScreenFormatConverter() {
        super(ScreenFormat.class);
    }
}
