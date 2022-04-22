package study.movie.global.enumMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.movie.global.dto.Response;

import static study.movie.global.constants.EnumClassConst.CITY_CODE;
import static study.movie.global.constants.ResponseMessage.READ_CATEGORY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/enums")
public class EnumController {

    private final EnumMapper enumMapper;

    @GetMapping("/cities")
    public ResponseEntity<?> getCities() {
        return Response.success(CITY_CODE.getValue() + READ_CATEGORY, enumMapper.get(CITY_CODE.getClassName()));
    }
}
