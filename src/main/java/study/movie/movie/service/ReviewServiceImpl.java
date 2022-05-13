package study.movie.movie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.movie.entity.Movie;
import study.movie.movie.entity.Review;
import study.movie.movie.dto.condition.ReviewSearchCond;
import study.movie.movie.dto.condition.ReviewSortType;
import study.movie.movie.dto.request.CreateReviewRequest;
import study.movie.movie.dto.request.UpdateReviewRequest;
import study.movie.movie.dto.response.ReviewResponse;
import study.movie.global.dto.PostIdResponse;
import study.movie.global.paging.DomainSpec;
import study.movie.global.paging.PageableDTO;
import study.movie.global.utils.BasicServiceUtils;
import study.movie.movie.repository.MovieRepository;
import study.movie.movie.repository.ReviewRepository;

import javax.persistence.EntityManager;

import static study.movie.global.exception.ErrorCode.MOVIE_NOT_FOUND;
import static study.movie.global.exception.ErrorCode.REVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl extends BasicServiceUtils implements ReviewService {
    private final EntityManager em;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final DomainSpec<ReviewSortType> spec = new DomainSpec<>(ReviewSortType.class);

    @Override
    @Transactional
    public PostIdResponse write(CreateReviewRequest request) {
        Movie findMovie = movieRepository
                .findById(request.getMovieId())
                .orElseThrow(getExceptionSupplier(MOVIE_NOT_FOUND));

        Review review = Review.writeReview()
                .movie(findMovie)
                .writer(request.getWriter())
                .score(request.getScore())
                .comment(request.getComment())
                .build();
        em.flush();
        return PostIdResponse.of(review.getId());
    }

    @Override
    @Transactional
    public void edit(Long id, UpdateReviewRequest request) {
        Review findReview = reviewRepository
                .findById(id)
                .orElseThrow(getExceptionSupplier(REVIEW_NOT_FOUND));

        findReview.edit(request.getScore(), request.getComment());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        reviewRepository.deleteByIdEqQuery(id);
    }

    @Override
    public Page<ReviewResponse> search(ReviewSearchCond cond, PageableDTO pageableDTO) {
        Pageable pageable = spec.getPageable(pageableDTO);
        return reviewRepository.search(cond, pageable).map(ReviewResponse::of);
    }
}
