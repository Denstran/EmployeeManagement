package com.manageemployee.employeemanagement.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Получаем информацию об ошибке из атрибута запроса
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorMessage", "Страница не найдена");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // Здесь можно добавить логику для обработки других типов ошибок
                model.addAttribute("errorMessage", "Произошла внутренняя ошибка сервера");
            }
        }
        return "error"; // Имя вашего HTML шаблона
    }

}

