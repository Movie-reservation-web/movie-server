package study.movie.domain.ticket.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import study.movie.domain.movie.entity.FilmRating;
import study.movie.domain.theater.entity.ScreenFormat;
import study.movie.domain.ticket.entity.Ticket;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ReserveTicketResponse {

    private Long id;

    /**
     * 예매 번호
     */
    private String reserveNumber;

    /**
     * 영화 정보
     */
    private FilmRating filmRating;
    private String movieTitle;
    private ScreenFormat screenFormat;
    private String movieImage;

    /**
     * 극장 정보
     */
    private String theaterName;
    private String screenName;

    /**
     * 예매 날짜 정보
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-EEE", timezone = "Asia/Seoul")
    private LocalDate date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime startTime;

    /**
     * 예매 인원 수
     */
    private Integer reservedMemberCount;

    /**
     * 좌석 정보
     */
    private String seats;

    /**
     * 결제 타입
     */
    private String paymentType;

    public static ReserveTicketResponse of(Ticket ticket) {
        return ReserveTicketResponse.builder()
                .id(ticket.getId())
                .reserveNumber(ticket.getReserveNumber())
                .filmRating(ticket.getMovie().getFilmRating())
                .movieTitle(ticket.getMovie().getTitle())
                .screenFormat(ticket.getFormat())
                .movieImage(ticket.getMovie().getImage())
                .theaterName(ticket.getTheaterName())
                .screenName(ticket.getScreenName())
                .date(ticket.getScreenTime().getStartDateTime().toLocalDate())
                .startTime(ticket.getScreenTime().getStartDateTime().toLocalTime())
                .endTime(ticket.getScreenTime().getEndDateTime().toLocalTime())
                .reservedMemberCount(ticket.getReservedMemberCount())
                .seats(String.join(",", ticket.getSeats()))
                .build();
    }
}
