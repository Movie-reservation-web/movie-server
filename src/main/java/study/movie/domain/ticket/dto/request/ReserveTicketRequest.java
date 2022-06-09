package study.movie.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ReserveTicketRequest {
    @NotNull
    @JsonProperty("merchant_uid")
    private String merchantUid;

    @NotNull
    @JsonProperty("schedule_number")
    private String scheduleNumber;

    @NotNull
    @JsonProperty("member_email")
    private String memberEmail;

    @NotNull
    private List<String> seats;
}
