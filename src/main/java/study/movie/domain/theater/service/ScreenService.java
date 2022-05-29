package study.movie.domain.theater.service;

import org.springframework.data.domain.Page;
import study.movie.domain.theater.dto.condition.ScreenSearchCond;
import study.movie.domain.theater.dto.request.CreateScreenRequest;
import study.movie.domain.theater.dto.request.UpdateScreenRequest;
import study.movie.domain.theater.dto.response.ScreenResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.PageableDTO;

public interface ScreenService {

    //save Screen
    PostIdResponse save(CreateScreenRequest request);

    //delete Screen
    void delete(Long id);

    //update Screen
    void update(Long id, UpdateScreenRequest request);

    //Screen findById
    ScreenResponse findById(Long id);

    Page<ScreenResponse> search(ScreenSearchCond cond, PageableDTO pageableDTO);
}
