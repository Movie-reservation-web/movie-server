package study.movie.app.screen.converter;

import study.movie.global.constants.EntityAttrConst.ScreenFormat;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class ScreenFormatConverter extends EnumArrayConverter<ScreenFormat> {
    public ScreenFormatConverter() {
        super(ScreenFormat.class);
    }
}
