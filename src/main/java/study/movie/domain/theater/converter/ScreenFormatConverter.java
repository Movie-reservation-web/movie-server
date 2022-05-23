package study.movie.domain.theater.converter;

import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ScreenFormatConverter extends CodeValueConverter<ScreenFormat> {
    public ScreenFormatConverter() {
        super(ScreenFormat.class);
    }
}
