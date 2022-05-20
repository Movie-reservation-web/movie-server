package study.movie.theater.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.movie.InitService;
import study.movie.movie.entity.Movie;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class TheaterTest {

    @Autowired
    EntityManager em;

    @Autowired
    InitService init;

    @Test
    public void 극장_생성 (){
        //given
        String theaterName = "왕십리CGV";
        CityCode city = CityCode.KYG; // 경기
        String phone = "000-0000-0000";

        Theater createTheater = Theater.builder()
                .name(theaterName)
                .city(city)
                .phone(phone)
                .build();

        em.persist(createTheater);

        //when
        Theater findTheater = em.find(Theater.class, createTheater.getId());

        //then
        assertThat(findTheater.getName()).isEqualTo(theaterName);
        assertThat(findTheater.getCity()).isEqualTo(city);
    }

    @Test
    public void 극장_수정 (){
        //given

        //when

        //then
    }
}