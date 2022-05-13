package study.movie.theater.converter;

import study.movie.theater.entity.ScreenFormat;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ScreenFormatConverter extends CodeValueConverter<ScreenFormat> {
    public ScreenFormatConverter() {
        super(ScreenFormat.class);
    }
}
