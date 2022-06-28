package study.movie.init;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Profile("local")
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
        initMovieService.initMovieData();
        initMovieService.initReviewData();
        initTheaterService.initTheaterData();
        initTheaterService.initScreenData();
        initScheduleService.initScheduleData();
    }
}
