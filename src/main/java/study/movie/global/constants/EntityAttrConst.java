package study.movie.global.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import study.movie.global.enumMapper.EnumMapperType;

public class EntityAttrConst {

    @AllArgsConstructor
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Genre implements EnumMapperType {
        ACTION("g01", "액션"),
        ADVENTURE("g02", "모험"),
        COMEDY("g03", "코미디"),
        DRAMA("g04", "드라마"),
        FANTASY("g05", "판타지"),
        HISTORY("g06", "역사"),
        HORROR("g07", "호러"),
        SCIENCE_FICTION("g08", "SF"),
        THRILLER("g09", "스릴러"),
        MUSICAL("g10", "뮤지컬"),
        SPORTS("g11", "스포츠"),
        WAR("g12", "전쟁"),
        CRIMINAL("g13", "범죄"),
        ROMANTIC_COMEDY("g14", "로맨틱코미디"),
        ROMANCE("g15", "멜로"),
        ANIMATION("g16", "애니메이션");
        private String code;
        private String desc;
    }

    @AllArgsConstructor
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum FilmRating implements EnumMapperType {
        UNDETERMINED("fr00", "미정"),
        G_RATED("fr01", "전체관람가"),
        PG_12("fr02", "12세 이상 관람가"),
        PG_15("fr03", "15세 이상 관람가"),
        X_RATED("fr04", "18세 이상 관람가"),
        R_RATED("fr05", "청소년 관람불가");
        private String code;
        private String desc;
    }

    @AllArgsConstructor
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum FilmFormat implements EnumMapperType {
        THREE_D("ff00", "3D"),
        IMAX("ff01", "IMAX"),
        FOUR_D_FLEX("ff02", "4DX"),
        SCREEN_X("ff03", "ScreenX"),
        FOUR_D_FLEX_SCREEN("f04", "4DX-Screen");
        private String code;
        private String desc;
    }

    @AllArgsConstructor
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum ScreenFormat implements EnumMapperType {
        THREE_D("ff00", "3D"),
        IMAX("sf01", "IMAX"),
        FOUR_D_FLEX("sf02", "4DX"),
        SCREEN_X("sf03", "ScreenX"),
        FOUR_D_FLEX_SCREEN("sf04", "4DX-Screen"),
        SUITE_CINEMA("sf05", "Suite Cinema"),
        CINE_DE_CHEF("sf06", "CINE de CHEF"),
        GOLD_CLASS("sf07", "GOLD Class"),
        SKY_BOX("sf08", "SKY BOX"),
        CINE_KIDS("sf09", "CINE Kids"),
        SPHERE_FLEX("sf10", "SPHERE X"),
        SOUND_FLEX("sf11", "SOUND X"),
        PREMIUM("sf12", "PREMIUM");
        private String code;
        private String desc;
    }

    public enum SeatStatus {
        EMPTY, RESERVING, RESERVED
    }
    public enum ReserveStatus {
        RESERVE, CANCEL
    }
}
