package study.movie.theater.service;

import study.movie.theater.dto.request.CreateTheaterRequest;
import study.movie.theater.dto.request.UpdateTheaterRequest;
import study.movie.theater.dto.response.BasicTheaterResponse;
import study.movie.theater.dto.response.TheaterNameResponse;
import study.movie.theater.entity.CityCode;

import java.util.List;

public interface TheaterService {

    //save Theater
    Long save(CreateTheaterRequest request);

    //delete Theater
    void delete(Long theaterId);

    //update Theater
    void update(Long id, UpdateTheaterRequest request);

    //find Theater groupBy 지역
    List<TheaterNameResponse> searchByCity(CityCode cityCode);

    //Theater findById
    BasicTheaterResponse findById(Long theaterId);
}
