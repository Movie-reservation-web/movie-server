package study.movie.controller.theater;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.theater.CityCode;
import study.movie.dto.theater.request.CreateTheaterRequest;
import study.movie.dto.theater.request.UpdateTheaterRequest;
import study.movie.dto.theater.response.BasicTheaterResponse;
import study.movie.dto.theater.response.TheaterNameResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.service.theater.TheaterService;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theater")
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateTheaterRequest request) {
        PostIdResponse result = PostIdResponse.of(theaterService.save(request));
        return CustomResponse.success(CREATED, CREATE_THEATER, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateTheaterRequest request) {
        theaterService.update(request);
        return CustomResponse.success(UPDATE_THEATER);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        theaterService.delete(id);
        return CustomResponse.success(DELETE_THEATER);
    }

    @GetMapping("/find")
    public ResponseEntity<?> search(@RequestParam @Valid Long theaterId) {
        BasicTheaterResponse result = theaterService.findById(theaterId);
        return CustomResponse.success(READ_THEATER, result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam @Valid CityCode cityCode) {
        List<TheaterNameResponse> result = theaterService.searchByCity(cityCode);
        return CustomResponse.success(READ_THEATER, result);
    }
}
