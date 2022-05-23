package study.movie.domain.member.dto.request;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginRequest {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    private String email;

    @NotBlank
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(
                email, password
        );
    }
}
