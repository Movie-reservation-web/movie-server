package study.movie.theater.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.utils.BasicServiceUtil;
import study.movie.theater.dto.request.CreateScreenRequest;
import study.movie.theater.dto.request.UpdateScreenRequest;
import study.movie.theater.dto.response.BasicScreenResponse;
import study.movie.theater.entity.Screen;
import study.movie.theater.entity.Theater;
import study.movie.theater.repository.ScreenRepository;
import study.movie.theater.repository.TheaterRepository;

import javax.persistence.EntityManager;

import static study.movie.exception.ErrorCode.SCREEN_NOT_FOUND;
import static study.movie.exception.ErrorCode.THEATER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScreenServiceImpl extends BasicServiceUtil implements ScreenService{
    private final EntityManager em;
    private  final TheaterRepository theaterRepository;
    private  final ScreenRepository screenRepository;

    @Override
    @Transactional
    public PostIdResponse save(CreateScreenRequest request) {
        Theater findTheater = theaterRepository
                    .findById(request.getTheaterId())
                    .orElseThrow(getExceptionSupplier(THEATER_NOT_FOUND));

        Screen createScreen = Screen.builder()
                .theater(findTheater)
                .format(request.getFormat())
                .name(request.getName())
                .maxRows(request.getMaxRows())
                .maxCols(request.getMaxCols())
                .build();

        em.flush();

        return PostIdResponse.of(createScreen.getId());
    }

    @Override
    public void delete(Long screenId) {
        screenRepository.deleteByIdEqQuery(screenId);
    }

    @Override
    public void update(Long id, UpdateScreenRequest request) {
        Screen findScreen = screenRepository.findById(id)
                .orElseThrow(getExceptionSupplier(SCREEN_NOT_FOUND));

        findScreen.update(request.getFormat(),  request.getMaxRows(), request.getMaxCols());
    }

    @Override
    public BasicScreenResponse findById(Long screenId) {
        return new BasicScreenResponse(screenRepository
                .findById(screenId)
                .orElseThrow(getExceptionSupplier(SCREEN_NOT_FOUND)));
    }
}
