package study.movie.converter.ticket;

import study.movie.domain.schedule.Seat;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SeatArrayConverter implements AttributeConverter<List<Seat>, String> {
    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<Seat> attribute) {
        return attribute.stream()
                .map(Seat::seatToString)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<Seat> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SEPARATOR))
                .map(Seat::stringToSeat)
                .collect(Collectors.toList());
    }
}
