package study.movie.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.movie.domain.theater.dto.request.CreateScreenRequest;
import study.movie.domain.theater.dto.request.CreateTheaterRequest;
import study.movie.domain.theater.entity.Screen;
import study.movie.domain.theater.entity.Theater;
import study.movie.domain.theater.repository.ScreenRepository;
import study.movie.domain.theater.repository.TheaterRepository;
import study.movie.global.utils.JsonUtil;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitTheaterService {
    private static final String THEATER = "theater";
    private static final String SCREEN = "screen";
    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;

    public void initTheaterData() {
        theaterRepository.saveAll(
                JsonUtil.jsonArrayToList(THEATER, CreateTheaterRequest.class).stream()
                        .map(CreateTheaterRequest::toEntity)
                        .collect(Collectors.toList()
                        )
        );
    }

    @Transactional
    public void initScreenData() {
        JsonUtil.jsonArrayToList(SCREEN, CreateScreenRequest.class).stream()
                .map(this::mapToScreen)
                .collect(Collectors.toList()
                );
    }

    private Screen mapToScreen(CreateScreenRequest request) {
        Theater theater = theaterRepository.findById(request.getTheaterId()).get();
        Screen screen = Screen.builder()
                .theater(theater)
                .format(request.getFormat())
                .name(request.getName())
                .maxRows(request.getMaxRows())
                .maxCols(request.getMaxCols())
                .build();
        return screen;
    }
}
