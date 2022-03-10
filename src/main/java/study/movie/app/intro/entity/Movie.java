package study.movie.app.intro.entity;

import lombok.Getter;
import study.movie.app.intro.converter.ActorArrayConverter;
import study.movie.app.intro.converter.GenreConverter;
import study.movie.global.constants.EntityAttrConst;
import study.movie.global.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.List;

import static study.movie.global.constants.EntityAttrConst.*;

@Entity
@Getter
@Table(name = "movies")
public class Movie extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    private String title;
    private String director;

    @Convert(converter = ActorArrayConverter.class)
    private List<String> actors;

    private Genre genre;



}
