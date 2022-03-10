package study.movie.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import study.movie.global.constants.EntityAttrConst.CommonType;

import java.util.EnumSet;
import java.util.NoSuchElementException;

/**
 * {@link CommonType} enum <-> String 변환하는 Util class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumValueConvertUtils {
    public static <T extends Enum<T> & CommonType> T ofCode(Class<T> enumClass,
                                                            String code) {
        if(!StringUtils.hasLength(code)) return null;
        return EnumSet.allOf(enumClass).stream()
                .filter(t -> t.getCode().equals(code))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    public static <T extends Enum<T> & CommonType> String toCode(T enumValue) {
        if (enumValue == null) return "";
        return enumValue.getCode();
    }
}
