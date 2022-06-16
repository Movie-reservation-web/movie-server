package study.movie.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.movie.domain.member.entity.GenderType;
import study.movie.domain.member.entity.Member;
import study.movie.domain.member.entity.Role;

import javax.validation.constraints.*;
import java.time.LocalDate;

@ApiModel(description = "회원가입요청 모델")
@Data
public class SignUpRequest {
    @Schema(description = "이름", required = true)
    @NotBlank
    @Pattern(regexp = "[a-zA-Z가-힣]{2,20}")
    private String name;

    @Schema(description = "이메일", required = true)
    @NotBlank
    @Pattern(regexp = "[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*")
    private String email;

    @Schema(description = "비밀번호", required = true)
    @NotBlank
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}")
    private String password;

    @Schema(description = "닉네임", required = true)
    @NotBlank
    @Size(max = 20)
    private String nickname;

    @Schema(description = "생년월일",  example = "yyyy-MM-dd",required = true)
    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birth;

    @Schema(description = "전화번호", example="000-0000-0000",required = true)
    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}")
    private String mobile;

    @Schema(description = "성별", example = "MALE or FEMALE",required = true)
    @NotNull
    private GenderType gender;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.basicBuilder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .nickname(this.nickname)
                .birth(this.birth)
                .gender(this.gender)
                .mobile(this.mobile)
                .role(Role.USER)
                .build();
    }
}
