package study.movie.global.enumMapper;

import lombok.Getter;

/**
 * Enum 을 바로 JSON으로 리턴하게 되면 name()만 출력됨.
 * 이를 해결하기 위해 DB의 컬럼값으로 사용하는 code와
 * view에서 사용될 desc를 같이 출력 (toString())
 *
 * -> view layer에서 국제화를 통해 desc를 사용하지 않고
 * code 값만 사용한다 해도 해당 desc를 보고 해당 코드의
 * 내용이 무엇인지 파악가능
 */
@Getter
public class EnumMapperValue {

    private String code;
    private String desc;

    public EnumMapperValue(EnumMapperType enumMapperType) {
        this.code = enumMapperType.getCode();
        this.desc = enumMapperType.getDesc();
    }

    @Override
    public String toString() {
        return '{' +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

}
