package study.movie.global.utils;

public abstract class NumberUtil {

    public static long getRandomIndex(long maxSize){
        return (long) (Math.random() * (maxSize));
    }
}
