package study.movie.global.exception;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.movie.global.dto.Response;
import study.movie.global.dto.ValidationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.global.exception.ErrorCode.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(value = CustomException.class)
    public final ResponseEntity<?> handleCustomExceptions(CustomException e) {
        return Response.fail(e.getErrorCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<?> handleIllegalArgumentExceptions() {
        return Response.fail(ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    public final ResponseEntity<?> handleDataException() {
        return Response.fail(DUPLICATED_RESOURCE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return Response.validationFail(createValidationDetails(ex));
    }

    private List<ValidationResponse> createValidationDetails(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream()
                .map(fieldError -> new ValidationResponse(fieldError, messageSource))
                .collect(Collectors.toList());
    }
}
