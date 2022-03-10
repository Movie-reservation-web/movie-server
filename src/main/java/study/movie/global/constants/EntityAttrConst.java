package study.movie.global.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class EntityAttrConst {

    /**
     * Legacy Super System 의 공통코드를 리턴한다.
     * @return String 코드값
     */
    public interface CommonType {
        String getCode();
        String getDesc();
    }

    @AllArgsConstructor @Getter
    public enum Genre implements CommonType{
        ACTION("G0","액션"),
        ADVENTURE("G1","모험"),
        COMEDY("COM","코미디"),
        DRAMA("DRA","드라마"),
        FANTASY("FAN","판타지"),
        HISTORY("HIS","역사"),
        HORROR("HOR","호러"),
        SCIENCE_FICTION("SCI","SF"),
        THRILLER("THR","스릴러"),
        MUSICAL("MUS","뮤지컬"),
        SPORTS("SPO","스포츠"),
        WAR("WAR","전쟁"),
        CRIMINAL("CRI","범죄"),
        ROMANTIC_COMEDY("RC","로맨틱코미디"),
        ROMANCE("ROM","멜로"),
        ANIMATION("ANI","애니메이션");
        private String code;
        private String desc;
    }
}
