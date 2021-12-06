package com.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RestExceptionHandler extends DefaultHandlerExceptionResolver {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!(ex instanceof IOException)) {
            LOGGER.error(String.format("Received exception in REST controller, in path=%s, params: %s",
                    request.getRequestURI(), request.getQueryString()), ex);
        } else {
            LOGGER.warn(String.format("client aborted request before it was done, path: %s", request.getRequestURL()));
        }
        return super.doResolveException(request, response, handler, ex);
    }
}