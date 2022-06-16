package study.movie.domain.theater.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.movie.domain.theater.dto.response.TheaterNameResponse;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.service.TheaterService;
import study.movie.global.dto.CustomResponse;

import javax.validation.Valid;
import java.util.List;

import static study.movie.global.constants.ResponseMessage.READ_THEATER;

@Api(value = "Theater Api Controller", tags = "[Api] Theater")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theater")
public class TheaterApiController {

    private final TheaterService theaterService;

    @Operation(summary = "상영관 도시명으로 조회", description = "도시명으로으로 상영관 정보를 조회한다.")
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam @Valid CityCode cityCode) {
        List<TheaterNameResponse> result = theaterService.searchByCity(cityCode);
        return CustomResponse.success(READ_THEATER, result);
    }
}
