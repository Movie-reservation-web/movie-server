package study.movie.auth.exception;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        if (authException instanceof OAuth2AuthenticationException) {
            responseJson.put("errorCode", ((OAuth2AuthenticationException) authException).getError().getErrorCode());
            responseJson.put("description", ((OAuth2AuthenticationException) authException).getError().getDescription());
            responseJson.put("uri", ((OAuth2AuthenticationException) authException).getError().getUri());
        } else {
            responseJson.put("message", authException.getMessage());
        }
        response.getWriter().print(responseJson);
    }

}
