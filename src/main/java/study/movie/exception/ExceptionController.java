package study.movie.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static study.movie.exception.ErrorCode.INVALID_JSON_WEB_TOKEN;
import static study.movie.exception.ErrorCode.PERMISSION_NOT_ACCESSIBLE;

@ApiIgnore
@RestController
@RequiredArgsConstructor
@RequestMapping("/exception")
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
