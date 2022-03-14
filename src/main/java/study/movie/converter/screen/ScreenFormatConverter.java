package study.movie.converter.screen;

import study.movie.global.constants.EntityAttrConst.ScreenFormat;
import study.movie.global.converter.EnumArrayConverter;

import javax.persistence.Converter;

@Converter
public class ScreenFormatConverter extends EnumArrayConverter<ScreenFormat> {
    public ScreenFormatConverter() {
        super(ScreenFormat.class);
    }
}
