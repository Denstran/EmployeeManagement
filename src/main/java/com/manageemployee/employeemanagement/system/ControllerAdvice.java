package com.manageemployee.employeemanagement.system;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        log.error("Request: {} raised {}", req.getRequestURI(), ex.toString());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }
}
