package study.movie.service.theater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.Screen;
import study.movie.domain.theater.Theater;
import study.movie.dto.theater.request.CreateScreenRequest;
import study.movie.dto.theater.request.UpdateScreenRequest;
import study.movie.dto.theater.response.BasicScreenResponse;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.repository.theater.ScreenRepository;
import study.movie.repository.theater.TheaterRepository;

import static study.movie.global.exception.ErrorCode.SCREEN_NOT_FOUND;
import static study.movie.global.exception.ErrorCode.THEATER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScreenServiceImpl extends BasicServiceUtils implements ScreenService{

    private  final TheaterRepository theaterRepository;
    private  final ScreenRepository screenRepository;

    @Override
    public Long save(CreateScreenRequest request) {
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

        return createScreen.getId();
    }

    @Override
    public void delete(Long screenId) {
        screenRepository.deleteByIdEqQuery(screenId);
    }

    @Override
    public void update(UpdateScreenRequest request) {
        Screen findScreen = screenRepository.findById(request.getScreenId())
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
