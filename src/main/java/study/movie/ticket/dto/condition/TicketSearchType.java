package study.movie.ticket.dto.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TicketSearchType implements EnumMapperType {
    RESERVE_NUMBER("예매 번호"),
    MOVIE_TITLE("영화 제목"),
    THEATER_NAME("극장 이름"),
    SCREEN_FORMAT("상영관 포멧"),
    MEMBER_NAME("회원 이름"),
    SCREEN_START_TIME("시작 날짜"),
    SCREEN_END_TIME("끝 날짜");

    private String value;

    @Override
    public String getCode() {
        return name();
    }
}
