package com.fatec.smart_parking.core.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;


import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());  // Retorna 403 (Forbidden)
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{ \"message\": \"Acesso negado! Você não tem privilégios suficientes para acessar este recurso.\" }");
    }
}
