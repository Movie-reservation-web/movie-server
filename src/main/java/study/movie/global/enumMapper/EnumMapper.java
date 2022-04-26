package study.movie.global.enumMapper;

import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 프로젝트 내에서 enum클래스는 변동이 없기 때문에 사용할 때마다 생성해주는 것이아니라
 * EnumMapper 클래스를 담을 factory를 Bean 으로 등록해서 필요할 때마다 꺼내서 쓴다.
 */
@NoArgsConstructor
public class EnumMapper {
    private Map<String, List<EnumMapperValue>> factory = new LinkedHashMap<>();

    public void put(String key, Class<? extends EnumMapperType> e) {
        factory.put(key, toEnumValues(e));
    }

    private List<EnumMapperValue> toEnumValues(Class<? extends EnumMapperType> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(EnumMapperValue::new)
                .collect(Collectors.toList());
    }

    public List<EnumMapperValue> get(String key) {
        return factory.get(key);
    }

    public Map<String, List<EnumMapperValue>> get(List<String> keys) {
        if (keys == null || keys.size() == 0) return new LinkedHashMap<>();
        return keys.stream()
                .collect(Collectors.toMap(Function.identity(), key -> factory.get(key)));
    }

    public Map<String, List<EnumMapperValue>> getAll() {
        return factory;
    }


}

