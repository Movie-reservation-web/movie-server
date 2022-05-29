package study.movie.domain.theater.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.theater.dto.request.CreateTheaterRequest;
import study.movie.domain.theater.dto.request.UpdateTheaterRequest;
import study.movie.domain.theater.dto.response.TheaterNameResponse;
import study.movie.domain.theater.dto.response.TheaterResponse;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.service.TheaterService;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theater")
public class TheaterApiController {

    private final TheaterService theaterService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateTheaterRequest request) {
        PostIdResponse result = theaterService.save(request);
        return CustomResponse.success(CREATED, CREATE_THEATER, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateTheaterRequest request) {
        theaterService.update(id, request);
        return CustomResponse.success(UPDATE_THEATER);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        theaterService.delete(id);
        return CustomResponse.success(DELETE_THEATER);
    }

    @GetMapping("/find")
    public ResponseEntity<?> search(@RequestParam @Valid Long theaterId) {
        TheaterResponse result = theaterService.findById(theaterId);
        return CustomResponse.success(READ_THEATER, result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam @Valid CityCode cityCode) {
        List<TheaterNameResponse> result = theaterService.searchByCity(cityCode);
        return CustomResponse.success(READ_THEATER, result);
    }
}
