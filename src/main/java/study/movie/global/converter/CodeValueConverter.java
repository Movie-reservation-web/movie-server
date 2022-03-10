package study.movie.global.converter;

import study.movie.global.constants.EntityAttrConst;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;


public class CodeValueConverter<E extends Enum<E> & EntityAttrConst.CommonType> implements AttributeConverter<E, String> {

    private Class<E> clazz;

    public CodeValueConverter(Class<E> enumClazz) {
        this.clazz = enumClazz;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute.getCode();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(clazz).stream()
                .filter(e -> e.getCode().equals(dbData))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
