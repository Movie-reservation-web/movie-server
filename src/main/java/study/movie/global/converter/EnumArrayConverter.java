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

    /**
     * List 로 들어온 파라미터에 대해 code값을 mapping 시켜 구분자로 연결
     * @param attribute
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(List<E> attribute) {
        return attribute.stream().map(E::name).collect(Collectors.joining(SEPARATOR));
    }

    /**
     * DB 에서 들어온 데이터르 구분자(,)로 나눈 뒤 값을 convertToEnum 메소드로 mapping 시킨 후
     * 값을 리스트에 담아 리턴
     * @param dbData
     * @return List<Enum>
     */
    @Override
    public List<E> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SEPARATOR)).map(this::convertToEnum).collect(Collectors.toList());
    }

    /**
     * 위에서 선언한 type parameter(enum class)에서 parameter 에 해당하는 값을 찾아 리턴
     * @param data
     * @return Enum
     */
    private E convertToEnum(String data) {
        return EnumSet.allOf(clazz).stream()
                .filter(e -> e.name().equals(data))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
