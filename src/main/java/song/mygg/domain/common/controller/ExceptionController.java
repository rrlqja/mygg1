package song.mygg.domain.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import song.mygg.domain.common.exception.post.PostException;
import song.mygg.domain.common.exception.rest.RestException;

@Slf4j
@ControllerAdvice
public class ExceptionController {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RestException.class)
    public String restException(RestException restException,
                                Model model) {
        model.addAttribute("message", restException.getMessage());

        return "error/4xx";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PostException.class)
    public String postException(PostException postException,
                                Model model) {
        model.addAttribute("message", postException.getMessage());

        return "error/4xx";
    }

}
