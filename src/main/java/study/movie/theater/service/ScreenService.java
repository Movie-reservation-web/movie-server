package study.movie.theater.service;

import study.movie.global.dto.PostIdResponse;
import study.movie.theater.dto.request.CreateScreenRequest;
import study.movie.theater.dto.request.UpdateScreenRequest;
import study.movie.theater.dto.response.BasicScreenResponse;

public interface ScreenService {

    //save Screen
    PostIdResponse save(CreateScreenRequest request);

    //delete Screen
    void delete(Long screenId);

    //update Screen
    void update(Long id, UpdateScreenRequest request);

    //Screen findById
    BasicScreenResponse findById(Long screenId);
}
