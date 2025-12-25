package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BookNotFoundException.class)
    public ModelAndView handleNotFoundException(BookNotFoundException ex) {
        return new ModelAndView("customError", "errorText", "Book not found");
    }

}
