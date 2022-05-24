package study.movie.domain.theater.service;

import study.movie.domain.theater.dto.request.CreateScreenRequest;
import study.movie.domain.theater.dto.request.UpdateScreenRequest;
import study.movie.domain.theater.dto.response.BasicScreenResponse;
import study.movie.global.dto.PostIdResponse;

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
