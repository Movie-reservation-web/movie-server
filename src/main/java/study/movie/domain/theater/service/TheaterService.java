package study.movie.domain.theater.service;

import org.springframework.data.domain.Page;
import study.movie.domain.theater.dto.condition.TheaterSearchCond;
import study.movie.domain.theater.dto.request.CreateTheaterRequest;
import study.movie.domain.theater.dto.request.UpdateTheaterRequest;
import study.movie.domain.theater.dto.response.TheaterNameResponse;
import study.movie.domain.theater.dto.response.TheaterResponse;
import study.movie.domain.theater.entity.CityCode;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

import java.util.List;

public interface TheaterService {

    //save Theater
    PostIdResponse save(CreateTheaterRequest request);

    //delete Theater
    void delete(Long id);

    //update Theater
    void update(Long id, UpdateTheaterRequest request);

    //find Theater groupBy 지역
    List<TheaterNameResponse> searchByCity(CityCode cityCode);

    //Theater findById
    TheaterResponse findById(Long id);

    Page<TheaterResponse> search(TheaterSearchCond cond, PageableDTO pageableDTO);
}
