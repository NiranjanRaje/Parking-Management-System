package org.example.parkingmanagementsystem.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectURL = request.getContextPath();

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin"))) {
            redirectURL = "/parking-management-system/admin/home";
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_user"))) {
            redirectURL = "/parking-management-system/participant/home";
        }

        response.sendRedirect(redirectURL);
    }
}