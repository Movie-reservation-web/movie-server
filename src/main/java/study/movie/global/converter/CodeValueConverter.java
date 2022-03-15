package study.movie.global.converter;

import study.movie.global.enumMapper.EnumMapperType;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;


public class CodeValueConverter<E extends Enum<E> & EnumMapperType> implements AttributeConverter<E, String> {

    private Class<E> clazz;

    /**
     * type parameter 로 들어온 값을 선언
     * @param enumClazz
     */
    public CodeValueConverter(Class<E> enumClazz) {
        this.clazz = enumClazz;
    }

    /**
     * Enum 의 code 값을 리턴
     * @param attribute
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute.getCode();
    }

    /**
     * 위에서 선언한 type parameter(enum class)에서 parameter 에 해당하는 값을 찾아 리턴
     * @param dbData
     * @return Enum
     */
    @Override
    public E convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(clazz).stream()
                .filter(e -> e.getCode().equals(dbData))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
