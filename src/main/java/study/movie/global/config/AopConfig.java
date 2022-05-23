package study.movie.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.movie.aop.LogTraceAspect;
import study.movie.aop.trace.logtrace.LogTrace;
import study.movie.aop.trace.logtrace.ThreadLocalLogTrace;

@Configuration
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

    @Bean
    public LogTrace logTrace(){
        return new ThreadLocalLogTrace();
    }
}
