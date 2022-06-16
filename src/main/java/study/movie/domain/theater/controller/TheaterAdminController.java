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
import study.movie.domain.theater.dto.condition.TheaterSearchCond;
import study.movie.domain.theater.dto.request.CreateTheaterRequest;
import study.movie.domain.theater.dto.request.UpdateTheaterRequest;
import study.movie.domain.theater.dto.response.TheaterResponse;
import study.movie.domain.theater.service.TheaterService;
import study.movie.global.dto.CustomResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static study.movie.global.constants.ResponseMessage.*;

@Api(value = "Theater Admin Controller", tags = "[Admin] Theater")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/theater")
public class TheaterAdminController {

    private final TheaterService theaterService;

    @Operation(summary = "상영관 저장", description = "상영관 정보를 저장한다.")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateTheaterRequest request) {
        PostIdResponse result = theaterService.save(request);
        return CustomResponse.success(CREATED, CREATE_THEATER, result);
    }

    @Operation(summary = "상영관 수정", description = "상영관 정보를 수정한다.")
    @Parameters({@Parameter(name = "id", description = "상영관의 id", required = true, in = ParameterIn.PATH)})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateTheaterRequest request) {
        theaterService.update(id, request);
        return CustomResponse.success(UPDATE_THEATER);
    }

    @Operation(summary = "상영관 삭제", description = "상영관 정보를 삭제한다.")
    @Parameters({@Parameter(name = "id", description = "상영관의 id", required = true, in = ParameterIn.PATH)})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        theaterService.delete(id);
        return CustomResponse.success(DELETE_THEATER);
    }

    @Operation(summary = "상영관 검색", description = "상영관 정보를 검색한다.")
    @Parameters({@Parameter(name = "id", description = "상영관의 id", required = true, in = ParameterIn.PATH)})
    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        TheaterResponse result = theaterService.findById(id);
        return CustomResponse.success(READ_THEATER, result);
    }

    @Operation(summary = "상영관 조건 조회", description = "조건(이름, 도시, 전화번호)으로 상영관 정보를 조회한다.")
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody TheaterSearchCond cond, PageableDTO pageableDTO) {
        Page<TheaterResponse> result = theaterService.search(cond, pageableDTO);
        return CustomResponse.success(READ_THEATER, result);
    }
}
