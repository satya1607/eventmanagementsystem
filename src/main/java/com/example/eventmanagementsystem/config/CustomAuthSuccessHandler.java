package com.example.eventmanagementsystem.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler{

	@Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if ("ROLE_ADMIN".equals(role)) {
            response.sendRedirect("/admindashboard");
        } else {
            response.sendRedirect("/userdashboard");
        }
    }


}
