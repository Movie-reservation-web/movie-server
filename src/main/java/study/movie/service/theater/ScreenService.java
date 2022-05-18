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

public interface ScreenService {

    //save Screen
    Long save(CreateScreenRequest request);

    //delete Screen
    void delete(Long screenId);

    //update Screen
    void update(UpdateScreenRequest request);

    //Screen findById
    BasicScreenResponse findById(Long screenId);
}
