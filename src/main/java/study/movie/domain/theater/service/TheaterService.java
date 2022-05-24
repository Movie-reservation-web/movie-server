package study.movie.domain.theater.service;

import study.movie.domain.theater.dto.request.CreateTheaterRequest;
import study.movie.domain.theater.dto.request.UpdateTheaterRequest;
import study.movie.domain.theater.dto.response.BasicTheaterResponse;
import study.movie.domain.theater.dto.response.TheaterNameResponse;
import study.movie.domain.theater.entity.CityCode;
import study.movie.global.dto.PostIdResponse;

import java.util.List;

public interface TheaterService {

    //save Theater
    PostIdResponse save(CreateTheaterRequest request);

    //delete Theater
    void delete(Long theaterId);

    //update Theater
    void update(Long id, UpdateTheaterRequest request);

    //find Theater groupBy 지역
    List<TheaterNameResponse> searchByCity(CityCode cityCode);

    //Theater findById
    BasicTheaterResponse findById(Long theaterId);
}
