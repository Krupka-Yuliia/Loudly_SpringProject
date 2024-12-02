package com.loudlyapp.errors;

import com.loudlyapp.utils.InvalidInputException;
import com.loudlyapp.utils.ResourceNotFound;
import com.loudlyapp.utils.SongNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 409
    @ExceptionHandler(SongNotFoundException.class)
    public ModelAndView handleSongNotFound(HttpServletRequest req, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 409
    @ExceptionHandler(ResourceNotFound.class)
    public ModelAndView handleNotFound(HttpServletRequest req, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    public ModelAndView defaultHandler(HttpServletRequest req, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.addObject("title", "Sorry, unexpected error");
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  //400
    @ExceptionHandler(InvalidInputException.class)
    public ModelAndView handleInvalidInput(HttpServletRequest req, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.addObject("title", "Invalid input");
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}
