package study.movie.converter.ticket;

import org.springframework.stereotype.Component;
import study.movie.domain.schedule.Seat;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
@Component
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
        return convertToStringSeat(Arrays.stream(dbData.split(SEPARATOR)).collect(Collectors.toList()));
    }

    public List<Seat> convertToStringSeat(List<String> data) {
        return data.stream().map(Seat::stringToSeat)
                .collect(Collectors.toList());
    }
}
