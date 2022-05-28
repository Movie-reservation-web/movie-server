package study.movie.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.movie.domain.member.entity.GenderType;
import study.movie.domain.member.entity.Member;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class OAuth2RegisterRequest {

    @NotBlank
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}")
    private String password;

    @NotBlank
    @Size(max = 20)
    private String nickname;

    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}")
    private String phone;

    @NotNull
    private GenderType gender;

    public Member toEntity(Member member, PasswordEncoder passwordEncoder) {
        member.registerBasicInfo(passwordEncoder.encode(password), nickname, phone, birth, gender);
        return member;
    }
}
