package study.movie.ticket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TicketStatus implements EnumMapperType{
    RESERVED("예매 완료"),
    CANCEL("예매 취소"),
    DELETE("티켓 삭제");
    private String value;

    @Override
    public String getCode() {
        return name();
    }

}

