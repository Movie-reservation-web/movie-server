package study.movie.service.theater;

import study.movie.domain.theater.CityCode;
import study.movie.dto.theater.request.CreateScreenRequest;
import study.movie.dto.theater.request.CreateTheaterRequest;
import study.movie.dto.theater.request.UpdateScreenRequest;
import study.movie.dto.theater.request.UpdateTheaterRequest;
import study.movie.dto.theater.response.BasicScreenResponse;
import study.movie.dto.theater.response.BasicTheaterResponse;
import study.movie.dto.theater.response.TheaterNameResponse;

import java.util.List;

public interface TheaterService {

    //save Theater
    Long save(CreateTheaterRequest request);

    //delete Theater
    void delete(Long theaterId);

    //update Theater
    void update(UpdateTheaterRequest request);

    //find Theater groupBy 지역
    List<TheaterNameResponse> searchByCity(CityCode cityCode);

    //Theater findById
    BasicTheaterResponse findById(Long theaterId);
}
