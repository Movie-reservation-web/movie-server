package study.movie.converter.schedule;

import study.movie.domain.schedule.Seat;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SeatConverter implements AttributeConverter<Seat, String> {
    @Override
    public String convertToDatabaseColumn(Seat attribute) {
        return attribute.seatToString();
    }

    @Override
    public Seat convertToEntityAttribute(String dbData) {
        return Seat.builder()
                .rowNum(dbData.charAt(0) - '@')
                .colNum(Integer.parseInt(dbData.substring(1)))
                .build();
    }
}
