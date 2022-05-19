package study.movie.theater.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.global.utils.BasicServiceUtil;
import study.movie.theater.dto.request.CreateTheaterRequest;
import study.movie.theater.dto.request.UpdateTheaterRequest;
import study.movie.theater.dto.response.BasicTheaterResponse;
import study.movie.theater.dto.response.TheaterNameResponse;
import study.movie.theater.entity.CityCode;
import study.movie.theater.entity.Theater;
import study.movie.theater.repository.TheaterRepository;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.exception.ErrorCode.THEATER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TheaterServiceImpl extends BasicServiceUtil implements TheaterService{

    private  final TheaterRepository theaterRepository;

    @Override
    public Long save(CreateTheaterRequest request) {
        Theater Createtheater = Theater.builder()
                                .name(request.getName())
                                .city(request.getCity())
                                .phone(request.getPhone())
                                .build();

        Theater theater = theaterRepository.save(Createtheater);

        return theater.getId();
    }

    @Override
    public void delete(Long theaterId) {
        theaterRepository.deleteById(theaterId);
    }

    @Override
    public void update(Long id, UpdateTheaterRequest request) {
        Theater findTheater = theaterRepository.findById(id)
                .orElseThrow(getExceptionSupplier(THEATER_NOT_FOUND));

        findTheater.update(request.getPhone());
    }

    @Override
    public List<TheaterNameResponse> searchByCity(CityCode cityCode) {
        return theaterRepository.findByCity(cityCode)
                .stream()
                .map(TheaterNameResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public BasicTheaterResponse findById(Long theaterId) {
        return new BasicTheaterResponse(theaterRepository
                .findById(theaterId)
                .orElseThrow(getExceptionSupplier(THEATER_NOT_FOUND)));
    }
}
