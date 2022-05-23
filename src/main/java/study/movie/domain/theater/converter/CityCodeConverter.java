package study.movie.domain.theater.converter;

import study.movie.domain.theater.entity.CityCode;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class CityCodeConverter extends CodeValueConverter<CityCode> {
    public CityCodeConverter() {
        super(CityCode.class);
    }
}
