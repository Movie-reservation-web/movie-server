package study.movie.movie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MovieGenre implements EnumMapperType {
    ACTION("액션"),
    ADVENTURE("모험"),
    COMEDY("코미디"),
    DRAMA("드라마"),
    FANTASY("판타지"),
    HISTORY("역사"),
    HORROR("호러"),
    SCIENCE_FICTION("SF"),
    THRILLER("스릴러"),
    MUSICAL("뮤지컬"),
    SPORTS("스포츠"),
    WAR("전쟁"),
    CRIMINAL("범죄"),
    ROMANTIC_COMEDY("로맨틱코미디"),
    ROMANCE("멜로"),
    ANIMATION("애니메이션");
    private String value;

    @Override
    public String getCode() {
        return name();
    }
}