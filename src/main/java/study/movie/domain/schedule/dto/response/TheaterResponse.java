package study.movie.domain.schedule.dto.response;

import lombok.*;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Theater;

@Data
@Builder
public class TheaterResponse {
    private Long id;
    private String name;
    private CityCode city;
    private String phone;

    public static TheaterResponse of(Theater theater) {
        return TheaterResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .city(theater.getCity())
                .phone(theater.getPhone())
                .build();
    }
}
