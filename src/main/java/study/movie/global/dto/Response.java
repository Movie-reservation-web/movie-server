package study.movie.global.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import study.movie.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static study.movie.global.constants.ResponseMessage.SUCCESS;
import static study.movie.global.exception.ErrorCode.ARGUMENTS_NOT_VALID;

@Component
public class Response {
    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class Result<T> {

        private int status;
        private LocalDateTime timeStamp;
        private String message;
        private T data;
        private String error;
        private String code;

        @Builder(builderClassName = "ResponseBuilder", builderMethodName = "ResponseBuilder")
        public Result(int status, LocalDateTime timeStamp, String message, T data, String code) {
            this.status = status;
            this.code = code;
            this.timeStamp = timeStamp;
            this.message = message;
            this.data = data;
        }

        @Builder(builderClassName = "ErrorBuilder", builderMethodName = "ErrorBuilder")
        private Result(int status, String message, String error, String code) {
            this.timeStamp = LocalDateTime.now();
            this.status = status;
            this.error = error;
            this.code = code;
            this.message = message;
        }

        @Builder(builderClassName = "ValidationBuilder", builderMethodName = "ValidationBuilder")
        private Result(int status, String message, String error, String code, T data) {
            this.timeStamp = LocalDateTime.now();
            this.status = status;
            this.error = error;
            this.code = code;
            this.message = message;
            this.data = data;
        }

        // 이 부분도 error 처럼 변경해야함
        private static <T> Result<T> resultResponse(HttpStatus status, String message, T data) {
            return Result.<T>ResponseBuilder()
                    .timeStamp(LocalDateTime.now())
                    .status(status.value())
                    .message(message)
                    .data(data)
                    .build();
        }
    }

    public static <T> ResponseEntity<?> success(HttpStatus status, String message, T data) {
        return ResponseEntity.ok(Result.resultResponse(status, message, data));
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
     *
     * @return data, message
     */
    public static <T> ResponseEntity<?> success(String message, T data) {
        return success(HttpStatus.OK, message, data);
    }


    public static ResponseEntity<Object> fail(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(Result.ErrorBuilder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getDetail())
                        .build()
                );

    }

    public static ResponseEntity<Object> validationFail(List<ValidationResponse> detail) {
        ErrorCode errorCode = ARGUMENTS_NOT_VALID;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(Result.ValidationBuilder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getDetail())
                        .data(detail)
                        .build()
                );

    }
}
