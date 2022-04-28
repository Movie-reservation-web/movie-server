package study.movie.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode{

    /**
     * <p>
     * Status: 400 BAD_REQUEST
     * <p>
     * Detail: 유효성 검증 실패
     */
    ARGUMENTS_NOT_VALID(BAD_REQUEST, "유효성 검증 실패"),

    /**
     * <p>
     * Status: 500 INTERNAL_SERVER_ERROR
     * <p>
     * Detail: 서버 오류
     */
    UNKNOWN_SERVER_ERROR(INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),

    /**
     * <p>
     * Status: 400 BAD_REQUEST
     * <p>
     * Detail: 잘못된 요청
     */
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    ALREADY_CANCELLED_TICKET(BAD_REQUEST, "이미 취소된 티켓입니다."),

    /**
     * <p>
     * Status: 404 NOT_FOUND
     * <p>
     * Detail: Resource 를 찾을 수 없음
     */
    SCHEDULE_NOT_FOUND(NOT_FOUND, "해당 상영일정을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    MOVIE_NOT_FOUND(NOT_FOUND, "해당 영화를 찾을 수 없습니다."),
    TICKET_NOT_FOUND(NOT_FOUND, "해당 티켓을 찾을 수 없습니다."),
    SCREEN_NOT_FOUND(NOT_FOUND, "해당 상영관을 찾을 수 없습니다."),
    THEATER_NOT_FOUND(NOT_FOUND, "해당 극장을 찾을 수 없습니다."),

    /**
     * <p>
     * Status: 409 CONFLICT
     * <p>
     * Detail: Resource 충돌, 중복 데이터 존재
     */
    DUPLICATED_RESOURCE(CONFLICT, "DB에 해당 데이터가 이미 존재합니다.");

    private final HttpStatus status;
    private final String detail;
}
