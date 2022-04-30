package study.movie.global.dto;

import lombok.Data;

import java.util.List;

@Data
public class IdListRequest {

    private List<Long> ids;
}
