package study.movie.global.enumMapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class EnumMapperTest {

    @Test
    void EnumMapperValue_json형태로_리턴() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (TestGenre value : TestGenre.values()) {
            String jsonDto = objectMapper.writeValueAsString(value);
            System.out.println("jsonDto = " + jsonDto);
        }

    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    enum TestGenre implements EnumMapperType {
        ACTION("ACT","액션"),
        ADVENTURE("ADV","모험"),
        COMEDY("COM","코미디"),
        DRAMA("DRA","드라마"),
        FANTASY("FAN","판타지");
        private String code;
        private String desc;

        TestGenre(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getValue() {
            return desc;
        }
    }


}