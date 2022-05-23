package study.movie.aop.pointcuts;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* study.movie.*.controller.*.*(..))")
    public void allController(){}
    @Pointcut("execution(* study.movie.*.service.*.*(..))")
    public void allService(){}
    @Pointcut("execution(* study.movie.*.repository.*.*(..))")
    public void allRepository(){}
}
