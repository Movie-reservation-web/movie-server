package study.movie.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

import static study.movie.global.constants.ResponseMessage.SUCCESS;

@Component
public class Response {
    @Data
    @AllArgsConstructor
    @Builder
    private static class Result<T> {

        private HttpStatus statusCode;
        private LocalDateTime timeStamp;
        private String message;
        private T data;

        private static <T> Result<T> response(HttpStatus statusCode, String message, T t) {
            return Result.<T>builder()
                    .statusCode(statusCode)
                    .timeStamp(LocalDateTime.now())
                    .message(message)
                    .data(t)
                    .build();
        }
    }

    public static <T> ResponseEntity<?> success(HttpStatus status, String message, T data) {
        return ResponseEntity.ok(Result.response(status, message, data));
    }

    /**
     * 상태 리턴
     *
     * @return status
     */
    public static ResponseEntity<?> success() {
        return success(HttpStatus.OK, SUCCESS, Collections.emptyList());
    }

    /**
     * 메시지, 상태 리턴
     *
     * @return message, status
     */
    public static ResponseEntity<?> success(String message) {
        return success(HttpStatus.OK, message, Collections.emptyList());
    }

    /**
     * 데이터, 상태 리턴
     *
     * @return data, status
     */
    public static <T> ResponseEntity<?> success(T data) {
        return success(HttpStatus.OK, SUCCESS, data);
    }

    /**
     * 데이터, 메시지 리턴
     * @return data, message
     */
    public static <T> ResponseEntity<?> success(String message, T data) {
        return success(HttpStatus.OK, message, data);
    }


    public static <T> ResponseEntity<?> fail(HttpStatus status, String message, T data) {
        return new ResponseEntity(Result.response(status, message, data), status);
    }
}
