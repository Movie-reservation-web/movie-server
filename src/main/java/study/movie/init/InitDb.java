package study.movie.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitMemberService initMemberService;
    private final InitMovieService initMovieService;
    private final InitTheaterService initTheaterService;
    private final InitScheduleService initScheduleService;

    @PostConstruct
    public void init() {
        initMemberService.initMemberData();
        initMovieService.initMovieDate();
        initTheaterService.initTheaterData();
        initTheaterService.initScreenData();
        initScheduleService.initScheduleData();
    }
}
