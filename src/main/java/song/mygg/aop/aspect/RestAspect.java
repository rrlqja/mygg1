package song.mygg.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RestAspect {

    @Around("execution(* song.mygg.domain.common.service..*(..))")
    public Object RestAop(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[REST Aspect] {}.{}(arg = {})", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), joinPoint.getArgs());

        Object proceed = joinPoint.proceed();

        log.info("[REST Aspect] {}.{} post = {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), proceed.toString());

        return proceed;
    }
}
