package study.movie.global.page;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;

import static study.movie.global.page.SortOption.*;

public class PageableArgumentResolver implements HandlerMethodArgumentResolver {
    private final String PAGE = "page";
    private final String SIZE = "size";
    private final String TOTAL_ELEMENTS = "total_elements";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PageableDTO.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        List<SortPair<String, SortOption>> sorts = new ArrayList<>();
        final var sortArr = webRequest.getParameterMap().get("sort");

        if (sortArr != null) {
            for (var v : sortArr) {
                String[] keywords = v.split(",");
                sorts.add(SortPair.of(keywords[0], (keywords.length < 2 || keywords[1].equals("asc")) ? ASC : DESC));
            }
        }

        return PageableDTO.builder()
                .page(getValue(webRequest.getParameter(PAGE)))
                .size(getValue(webRequest.getParameter(SIZE)))
                .totalElements(getValue(webRequest.getParameter(TOTAL_ELEMENTS)))
                .sorts(sorts)
                .build();
    }

    private Integer getValue(String param) {
        return param != null ? Integer.parseInt(param) : null;
    }
}
