package study.movie.domain.movie;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.movie.domain.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Movie extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "movie_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long time;

    @Lob
    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private LocalDate release;

    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    private String actors;

    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genres")
    @Enumerated(EnumType.STRING)
    List<GenreType> genres = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "movie_types", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "types")
    @Enumerated(EnumType.STRING)
    List<MovieType> types = new ArrayList<>();

    @Builder
    public Movie(String name, Long time, String info,
                 LocalDate release, String director, String actors,
                 List<GenreType> genres, List<MovieType> types) {
        this.name = name;
        this.time = time;
        this.info = info;
        this.release = release;
        this.director = director;
        this.actors = actors;
        this.genres = genres;
        this.types = types;
    }
}
