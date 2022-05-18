package study.movie.controller.theater;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.theater.CityCode;
import study.movie.dto.theater.request.CreateScreenRequest;
import study.movie.dto.theater.request.CreateTheaterRequest;
import study.movie.dto.theater.request.UpdateScreenRequest;
import study.movie.dto.theater.request.UpdateTheaterRequest;
import study.movie.dto.theater.response.BasicScreenResponse;
import study.movie.dto.theater.response.BasicTheaterResponse;
import study.movie.dto.theater.response.TheaterNameResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.service.theater.ScreenService;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;
import static study.movie.global.constants.ResponseMessage.DELETE_SCREEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/screen")
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateScreenRequest request) {
        PostIdResponse result = PostIdResponse.of(screenService.save(request));
        return CustomResponse.success(CREATED, CREATE_SCREEN, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateScreenRequest request) {
        screenService.update(request);
        return CustomResponse.success(UPDATE_SCREEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        screenService.delete(id);
        return CustomResponse.success(DELETE_SCREEN);
    }

    @GetMapping("/find")
    public ResponseEntity<?> search(@RequestParam @Valid Long screenId) {
        BasicScreenResponse result = screenService.findById(screenId);
        return CustomResponse.success(READ_SCREEN, result);
    }

}
