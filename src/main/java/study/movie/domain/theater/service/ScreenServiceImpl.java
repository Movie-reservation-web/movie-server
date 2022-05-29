package study.movie.domain.theater.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.dto.condition.ScreenSearchCond;
import study.movie.domain.theater.dto.condition.ScreenSortType;
import study.movie.domain.theater.dto.request.CreateScreenRequest;
import study.movie.domain.theater.dto.request.UpdateScreenRequest;
import study.movie.domain.theater.dto.response.ScreenResponse;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.Theater;
import study.movie.domain.theater.repository.ScreenRepository;
import study.movie.domain.theater.repository.TheaterRepository;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.DomainSpec;
import study.movie.global.paging.PageableDTO;
import study.movie.global.utils.BasicServiceUtil;

import static study.movie.exception.ErrorCode.SCREEN_NOT_FOUND;
import static study.movie.exception.ErrorCode.THEATER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScreenServiceImpl extends BasicServiceUtil implements ScreenService {
    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final DomainSpec<ScreenSortType> spec = new DomainSpec<>(ScreenSortType.class);

    @Override
    @Transactional
    public PostIdResponse save(CreateScreenRequest request) {
        Theater findTheater = theaterRepository
                .findById(request.getTheaterId())
                .orElseThrow(getExceptionSupplier(THEATER_NOT_FOUND));

        Screen screen = Screen.builder()
                .theater(findTheater)
                .format(request.getFormat())
                .name(request.getName())
                .maxRows(request.getMaxRows())
                .maxCols(request.getMaxCols())
                .build();

        Screen savedScreen = screenRepository.save(screen);
        return PostIdResponse.of(savedScreen.getId());
    }

    @Override
    public void delete(Long screenId) {
        screenRepository.deleteByIdEqQuery(screenId);
    }

    @Override
    public void update(Long id, UpdateScreenRequest request) {
        Screen findScreen = screenRepository.findById(id)
                .orElseThrow(getExceptionSupplier(SCREEN_NOT_FOUND));

        findScreen.update(request.getFormat(), request.getMaxRows(), request.getMaxCols());
    }

    @Override
    public ScreenResponse findById(Long id) {
        return ScreenResponse.of(
                screenRepository.findById(id)
                        .orElseThrow(getExceptionSupplier(SCREEN_NOT_FOUND))
        );
    }

    @Override
    public Page<ScreenResponse> search(ScreenSearchCond cond, PageableDTO pageableDTO) {
        Pageable pageable = spec.getPageable(pageableDTO);
        return screenRepository.search(cond, pageable)
                .map(ScreenResponse::of);
    }
}
