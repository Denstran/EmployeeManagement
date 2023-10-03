package com.manageemployee.employeemanagement.exception;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Iterator;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ModelAndView handleResourceAlreadyExistsException(ResourceAlreadyExistsException e, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView((String) session.getAttribute("viewName"));
        modelAndView.addObject("errorMessage", e.getMessage());

        Iterator<String> attributes = session.getAttributeNames().asIterator();

        while (attributes.hasNext()) {
            String attributeName = attributes.next();
            if ("viewName".equals(attributeName)) continue;
            modelAndView.addObject(session.getAttribute(attributeName));
        }
        return modelAndView;
    }
}
