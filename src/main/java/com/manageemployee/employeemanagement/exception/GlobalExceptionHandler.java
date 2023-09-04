package com.manageemployee.employeemanagement.exception;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ModelAndView handleResourceAlreadyExistsException(ResourceAlreadyExistsException e, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("createCompanyBranch");
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject(session.getAttribute("companyBranchDTO"));
        return modelAndView;
    }
}
