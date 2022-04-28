package study.movie.global.utils;

import study.movie.global.exception.CustomException;
import study.movie.global.exception.ErrorCode;

import java.util.function.Supplier;

public abstract class BasicServiceUtils {
    protected static Supplier<CustomException> getExceptionSupplier(ErrorCode errorCode) {
        return () -> new CustomException(errorCode);
    }
}
