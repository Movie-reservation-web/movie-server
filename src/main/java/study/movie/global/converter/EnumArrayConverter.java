package study.movie.global.converter;

import study.movie.global.enumMapper.EnumMapperType;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * DB에 저장(구분자 ',')되어 있는 String을 구분자로 나눈 뒤
 * 해당 Enum을 찾아 List에 담아준다
 */
public class EnumArrayConverter<E extends Enum<E> & EnumMapperType> implements AttributeConverter<List<E>, String> {
    private Class<E> clazz;
    private static final String SEPARATOR = ",";

    public EnumArrayConverter(Class<E> enumClazz) {
        this.clazz = enumClazz;
    }

    @Override
    public String convertToDatabaseColumn(List<E> attribute) {
        return attribute.stream().map(E::getCode).collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<E> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SEPARATOR)).map(this::convertToEnum).collect(Collectors.toList());
    }

    private E convertToEnum(String data) {
        return EnumSet.allOf(clazz).stream()
                .filter(e -> e.getCode().equals(data))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
