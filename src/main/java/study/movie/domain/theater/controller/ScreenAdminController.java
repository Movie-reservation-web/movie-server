package study.movie.domain.theater.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import study.movie.global.dto.IdListRequest;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@Api(value = "Screen Admin Controller", tags = "[Admin] Screen")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/screen")
public class ScreenAdminController {

    private final ScreenService screenService;

    @Operation(summary = "스크린 저장", description = "스크린 정보를 저장한다.")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateScreenRequest request) {
        PostIdResponse result = screenService.save(request);
        return CustomResponse.success(CREATED, CREATE_SCREEN, result);
    }

    @Operation(summary = "스크린 수정", description = "스크린 정보를 수정한다.")
    @Parameters({@Parameter(name = "id", description = "스크린의 id", required = true, in = ParameterIn.PATH)})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateScreenRequest request) {
        screenService.update(id, request);
        return CustomResponse.success(UPDATE_SCREEN);
    }

    @Operation(summary = "스크린 삭제", description = "스크린 정보를 삭제한다.")
    @Parameters({@Parameter(name = "id", description = "스크린의 id", required = true)})
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody @Valid IdListRequest request) {
        screenService.delete(request);
        return CustomResponse.success(DELETE_SCREEN);
    }

    @Operation(summary = "스크린 검색", description = "스크린의 id로 스크린 정보를 검색한다.")
    @Parameters({@Parameter(name = "id", description = "스크린의 id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        ScreenResponse result = screenService.findById(id);
        return CustomResponse.success(READ_SCREEN, result);
    }

    @Operation(summary = "스크린 조건 조회", description = "조건(스크린 타입, 상영관 이름)으로 스크린을 조회한다.")
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody ScreenSearchCond cond, PageableDTO pageableDTO) {
        Page<ScreenResponse> result = screenService.search(cond, pageableDTO);
        return CustomResponse.success(READ_SCREEN, result);
    }


}
