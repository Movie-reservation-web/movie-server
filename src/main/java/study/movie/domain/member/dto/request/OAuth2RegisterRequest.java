package study.movie.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.movie.domain.member.entity.GenderType;
import study.movie.domain.member.entity.Member;

import javax.validation.constraints.*;
import java.time.LocalDate;

@ApiModel(description = "OAuth2요청 모델")
@Data
public class OAuth2RegisterRequest {

    @Schema(description = "비밀번호", required = true)
    @NotBlank
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}")
    private String password;

    @Schema(description = "닉네임", required = true)
    @NotBlank
    @Size(max = 20)
    private String nickname;

    @Schema(description = "생년월일", required = true)
    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Schema(description = "전화번호", required = true)
    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}")
    private String phone;

    @Schema(description = "성별", required = true)
    @NotNull
    private GenderType gender;

    public Member toEntity(Member member, PasswordEncoder passwordEncoder) {
        member.registerBasicInfo(passwordEncoder.encode(password), nickname, phone, birth, gender);
        return member;
    }
}
