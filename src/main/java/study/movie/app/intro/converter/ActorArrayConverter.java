package study.movie.app.intro.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class ActorArrayConverter implements AttributeConverter<List<String>, String> {
    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute.stream().collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SEPARATOR)).collect(Collectors.toList());
    }
}
