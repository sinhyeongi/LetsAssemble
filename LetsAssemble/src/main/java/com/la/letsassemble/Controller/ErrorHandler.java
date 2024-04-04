package com.la.letsassemble.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class ErrorHandler implements ErrorController {
    @RequestMapping("/error")
    public String handleNotFoundException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object o = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(o != null){
            int code = Integer.parseInt(o.toString());
            if(code == 404){
                return "ERROR/404";
            }else if(code == 403){
                return "ERROR/403";
            }else if(code == 400){
                response.getWriter().print("err");
                return null;
            }
        }
        return "ERROR/err";
    }
}
