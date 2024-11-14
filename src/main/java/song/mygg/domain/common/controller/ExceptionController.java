package song.mygg.domain.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import song.mygg.domain.common.exception.post.PostException;
import song.mygg.domain.common.exception.post.PostNotFoundException;
import song.mygg.domain.common.exception.rest.RestException;
import song.mygg.domain.common.exception.rest.RestNotFoundException;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> restException(RestException restException) {
        if (restException instanceof RestNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", restException.getMessage()));
        }

        return ResponseEntity.badRequest()
                .body(Map.of("message", restException.getMessage()));
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<?> postException(PostException postException) {
        if (postException instanceof PostNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", postException.getMessage()));
        }

        return ResponseEntity.badRequest()
                .body(Map.of("message", postException.getMessage()));
    }

}
