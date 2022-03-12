package study.movie.domain.movie;

import lombok.RequiredArgsConstructor;
import study.movie.model.EnumModel;

@RequiredArgsConstructor
public enum GenreType implements EnumModel {
    ACTION("액션"),
    DRAMA("드라마"),
    COMEDY("코미디"),
    THRILLER("스릴러"),
    HORROR("공포"),
    SF("SF"),
    MELODRAMA("멜로"),
    MYSTERY("미스테리"),
    WESTERN("서부극"),
    ADVENTURE("어드벤쳐"),
    FANTASY("판타지"),
    ANIMATION("애니메이션");

    private final String value;

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }
}
