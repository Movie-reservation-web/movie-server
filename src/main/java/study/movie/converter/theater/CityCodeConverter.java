package study.movie.converter.theater;

import study.movie.domain.theater.CityCode;
import study.movie.global.converter.CodeValueConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class CityCodeConverter extends CodeValueConverter<CityCode> {
    public CityCodeConverter() {
        super(CityCode.class);
    }
}
