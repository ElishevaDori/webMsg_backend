//package com.example;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.NoHandlerFoundException;
//
//import javax.servlet.http.HttpServletRequest;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleException (NoHandlerFoundException ex, HttpServletRequest request) {
//        LOGGER.info("No mappings found for " + request.getRequestURI());
//        return "/home";
//    }
//}
