package study.movie.global.utils;

import study.movie.exception.CustomException;
import study.movie.exception.ErrorCode;

import java.util.function.Supplier;

public abstract class BasicServiceUtil {
    protected static Supplier<CustomException> getExceptionSupplier(ErrorCode errorCode) {
        return () -> new CustomException(errorCode);
    }
}
