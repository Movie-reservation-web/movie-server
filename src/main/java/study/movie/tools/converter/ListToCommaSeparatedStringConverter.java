package study.movie.tools.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.List;

public class ListToCommaSeparatedStringConverter
        implements Converter<List<String>, String> {

    @Override
    public String convert(List<String> source) {
        return String.join(", ", source);
    }
}
