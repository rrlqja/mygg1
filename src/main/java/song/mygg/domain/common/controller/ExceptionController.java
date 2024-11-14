package song.mygg.domain.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import song.mygg.domain.common.exception.rest.RestException;
import song.mygg.domain.common.exception.rest.RestNotFoundException;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ResponseBody
    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> restException(RestException restException) {
        if (restException instanceof RestNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "not found exception"));
        }

        return ResponseEntity.badRequest()
                .body(Map.of("message", "rest exception"));
    }

}
