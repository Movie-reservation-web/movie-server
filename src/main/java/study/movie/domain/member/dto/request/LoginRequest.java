package study.movie.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(description = "로그인요청 모델")
@Data
public class LoginRequest {

    @Schema(description = "아이디(이메일)", required = true)
    @NotBlank
    @Pattern(regexp = "[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호", required = true)
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                email, password
        );
    }
}
