package study.movie.service.theater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.CityCode;
import study.movie.domain.theater.Theater;
import study.movie.dto.theater.request.CreateTheaterRequest;
import study.movie.dto.theater.request.UpdateTheaterRequest;
import study.movie.dto.theater.response.BasicTheaterResponse;
import study.movie.dto.theater.response.TheaterNameResponse;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.repository.theater.TheaterRepository;

import java.util.List;
import java.util.stream.Collectors;

import static study.movie.global.exception.ErrorCode.THEATER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TheaterServiceImpl extends BasicServiceUtils implements TheaterService{

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
    public void update(UpdateTheaterRequest request) {
        Theater findTheater = theaterRepository.findById(request.getTheaterId())
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
