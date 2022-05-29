package study.movie.domain.theater.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.movie.domain.theater.dto.condition.ScreenSearchCond;
import study.movie.domain.theater.dto.request.CreateScreenRequest;
import study.movie.domain.theater.dto.request.UpdateScreenRequest;
import study.movie.domain.theater.dto.response.ScreenResponse;
import study.movie.domain.theater.service.ScreenService;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/screen")
public class ScreenAdminController {

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

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        ScreenResponse result = screenService.findById(id);
        return CustomResponse.success(READ_SCREEN, result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody ScreenSearchCond cond, PageableDTO pageableDTO) {
        Page<ScreenResponse> result = screenService.search(cond, pageableDTO);
        return CustomResponse.success(READ_SCREEN, result);
    }


}
