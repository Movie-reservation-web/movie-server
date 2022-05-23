package study.movie.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static study.movie.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exception")
@Slf4j
public class ExceptionController {
    @GetMapping("/entry-point")
    public ResponseEntity<?> entryPointException() {
        throw new CustomException(INVALID_JSON_WEB_TOKEN);
    }

    @GetMapping("/access-denied")
    public ResponseEntity<?> accessDeniedException() {
        throw new CustomException(PERMISSION_NOT_ACCESSIBLE);
    }
}
