package study.movie.converter.member;

import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

import static study.movie.global.constants.EntityAttrConst.GenderType;

@Converter(autoApply = true)
public class GenderConverter extends CodeValueConverter<GenderType> {
    public GenderConverter() {super(GenderType.class);}
}
