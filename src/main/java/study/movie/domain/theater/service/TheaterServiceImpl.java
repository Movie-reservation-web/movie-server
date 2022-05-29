package study.movie.domain.theater.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.dto.condition.TheaterSearchCond;
import study.movie.domain.theater.dto.condition.TheaterSortType;
import study.movie.domain.theater.dto.request.CreateTheaterRequest;
import study.movie.domain.theater.dto.request.UpdateTheaterRequest;
import study.movie.domain.theater.dto.response.TheaterNameResponse;
import study.movie.domain.theater.dto.response.TheaterResponse;
import study.movie.domain.theater.entity.CityCode;
import study.movie.domain.theater.entity.Theater;
import study.movie.domain.theater.repository.TheaterRepository;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.DomainSpec;
import study.movie.global.paging.PageableDTO;
import study.movie.global.utils.BasicServiceUtil;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.exception.ErrorCode.THEATER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TheaterServiceImpl extends BasicServiceUtil implements TheaterService {
    private final TheaterRepository theaterRepository;
    private final DomainSpec<TheaterSortType> spec = new DomainSpec<>(TheaterSortType.class);


    @Override
    @Transactional
    public PostIdResponse save(CreateTheaterRequest request) {
        Theater theater = request.toEntity();
        Theater saveTheater = theaterRepository.save(theater);
        return PostIdResponse.of(saveTheater.getId());
    }

    @Override
    public void delete(Long theaterId) {
        theaterRepository.deleteById(theaterId);
    }

    @Override
    @Transactional
    public void update(Long id, UpdateTheaterRequest request) {
        Theater findTheater = theaterRepository.findById(id)
                .orElseThrow(getExceptionSupplier(THEATER_NOT_FOUND));

        findTheater.update(request.getPhone());
    }

    @Override
    public List<TheaterNameResponse> searchByCity(CityCode cityCode) {
        return theaterRepository.findByCity(cityCode)
                .stream()
                .map(TheaterNameResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public TheaterResponse findById(Long theaterId) {
        return TheaterResponse.of(
                theaterRepository.findById(theaterId)
                        .orElseThrow(getExceptionSupplier(THEATER_NOT_FOUND)));
    }

    @Override
    public Page<TheaterResponse> search(TheaterSearchCond cond, PageableDTO pageableDTO) {
        Pageable pageable = spec.getPageable(pageableDTO);
        return theaterRepository.search(cond, pageable)
                .map(TheaterResponse::of);
    }

}
