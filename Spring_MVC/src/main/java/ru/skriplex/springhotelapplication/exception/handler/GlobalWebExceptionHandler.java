package ru.skriplex.springhotelapplication.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.skriplex.springhotelapplication.exception.HotelIsExistException;
import ru.skriplex.springhotelapplication.exception.HotelNotFoundException;

@Slf4j
@ControllerAdvice
public class GlobalWebExceptionHandler {

    @ExceptionHandler(HotelNotFoundException.class)
    public ModelAndView handleExceptionHotelNotFound(HotelNotFoundException ex) {
        log.error("HotelNotFoundException: {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "HotelNotFoundException");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", HttpStatus.NOT_FOUND.value());

        return mav;
    }

    @ExceptionHandler(HotelIsExistException.class)
    public ModelAndView handleExceptionHotelIsExist(HotelIsExistException ex) {
        log.error("HotelIsExistException {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "HotelIsExistException");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", HttpStatus.BAD_REQUEST.value());

        return mav;
    }

}
