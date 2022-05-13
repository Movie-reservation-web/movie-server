package study.movie.theater.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CityCode implements EnumMapperType {

    SEL("서울"),
    KYG("경기"),
    INC("인천"),
    KAW("강원"),
    CCN_TAJ("대전/충청"),
    TAE("대구"),
    PUS_USN("부산/울산"),
    KSB_KSN("경상"),
    KWJ_CL_CHJ("광주/전라/제주");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
