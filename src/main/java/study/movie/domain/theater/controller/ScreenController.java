package study.movie.domain.theater.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.theater.dto.request.CreateScreenRequest;
import study.movie.domain.theater.dto.request.UpdateScreenRequest;
import study.movie.domain.theater.dto.response.BasicScreenResponse;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.domain.theater.service.ScreenService;
import javax.validation.Valid;

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
        PostIdResponse result = screenService.save(request);
        return CustomResponse.success(CREATED, CREATE_SCREEN, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateScreenRequest request) {
        screenService.update(id, request);
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
