package study.movie.converter.member;

import study.movie.domain.member.GenderType;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter extends CodeValueConverter<GenderType> {
    public GenderConverter() {super(GenderType.class);}
}
