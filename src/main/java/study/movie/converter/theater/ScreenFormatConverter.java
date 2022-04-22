package study.movie.converter.theater;

import study.movie.domain.theater.ScreenFormat;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ScreenFormatConverter extends CodeValueConverter<ScreenFormat> {
    public ScreenFormatConverter() {
        super(ScreenFormat.class);
    }
}
