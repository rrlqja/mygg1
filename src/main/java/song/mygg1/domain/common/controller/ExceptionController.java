package song.mygg1.domain.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import song.mygg1.domain.common.exception.riot.SearchException;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({SearchException.class})
    public String handleSearchException(SearchException e) {
        log.error(e.getMessage());

        return "common/4xx";
    }

    @ExceptionHandler({RuntimeException.class})
    public String handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());

        return "common/5xx";
    }
}
