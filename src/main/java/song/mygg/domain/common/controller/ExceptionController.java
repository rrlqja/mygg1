package song.mygg.domain.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import song.mygg.domain.common.exception.rest.RestException;
import song.mygg.domain.common.exception.rest.RestNotFoundException;
import song.mygg.domain.common.response.CommonResponse;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ResponseBody
    @ExceptionHandler(RestException.class)
    public CommonResponse<String> restException(RestException restException) {
        if (restException instanceof RestNotFoundException) {
            return new CommonResponse<>(NOT_FOUND.value(), "not found", restException.getMessage());

        }

        return new CommonResponse<>(BAD_REQUEST.value(), "rest exception", restException.getMessage());
    }

}
