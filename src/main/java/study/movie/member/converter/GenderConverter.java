package study.movie.member.converter;

import study.movie.member.entity.GenderType;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter extends CodeValueConverter<GenderType> {
    public GenderConverter() {super(GenderType.class);}
}
