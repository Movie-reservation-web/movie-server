package study.movie.global.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final String JSON_DIR = "json/";
    private static final String JSON_EXTENSION = ".json";
    private static final String UTF_8 = "UTF-8";

    @SneakyThrows
    private static InputStreamReader readJsonFile(String fileName){
        ClassPathResource resource = new ClassPathResource(JSON_DIR + fileName + JSON_EXTENSION);
        return new InputStreamReader(resource.getInputStream(), UTF_8);
    }

    @SneakyThrows
    public static <T> List<T> jsonArrayToList(String fileName, Class<T> elementClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType listType =
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass);
        return objectMapper.readValue(readJsonFile(fileName), listType);
    }
}
