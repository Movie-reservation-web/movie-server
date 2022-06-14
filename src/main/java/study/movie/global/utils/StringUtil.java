package study.movie.global.utils;

public abstract class StringUtil {
    public static String convertDoubleToString(double num) {
        return String.format("%.1f", num) + "%";
    }
}
