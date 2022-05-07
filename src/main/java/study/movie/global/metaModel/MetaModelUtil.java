package study.movie.global.metaModel;

import com.querydsl.core.types.Path;

public class MetaModelUtil {
    public static String getColumn(Path<?> path) {
        return path.getMetadata().getName();
    }
}
