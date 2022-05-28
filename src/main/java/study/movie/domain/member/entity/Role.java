package study.movie.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role implements EnumMapperType {

    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER"),
    GUEST("ROLE_GUEST");

    private String value;

    @Override
    public String getCode() {
        return name();
    }
    public static String getRoleHierarchy(){
        return Arrays.stream(Role.values()).map(Role::getValue).collect(Collectors.joining(" > "));
    }

}
