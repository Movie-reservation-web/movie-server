package study.movie.domain.member.converter;

import study.movie.domain.member.entity.GenderType;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter extends CodeValueConverter<GenderType> {
    public GenderConverter() {super(GenderType.class);}
}
