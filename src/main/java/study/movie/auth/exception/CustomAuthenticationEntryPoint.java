package study.movie.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import study.movie.exception.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        log.info("request={}", request);
        ErrorCode errorCode;
        log.debug("log: exception: {} ", exception);

        /**
         * 토큰 없는 경우
         */
//        if (exception == null) {
//            errorCode = ErrorCode.NO_JSON_WEB_TOKEN;
//            setResponse(response, errorCode);
//            return;
//        }
//
//        /**
//         * 토큰 만료된 경우
//         */
//        if (exception.equals("ExpiredJwtException")) {
//            errorCode = ErrorCode.EXPIRED_JSON_WEB_TOKEN;
//            setResponse(response, errorCode);
//            return;
//        }
//
//        /**
//         * 토큰 시그니처가 다른 경우
//         */
//        if (exception.equals(ErrorCode.INVALID_TOKEN.getCode())) {
//            errorCode = ErrorCode.INVALID_TOKEN;
//            setResponse(response, errorCode);
//        }

        response.sendRedirect("/exception/entry-point");
    }
}
