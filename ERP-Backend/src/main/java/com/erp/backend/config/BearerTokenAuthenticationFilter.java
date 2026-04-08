package com.erp.backend.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public BearerTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();
            if (StringUtils.hasText(token)) {
                try {
                    String credentials = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
                    int colonIndex = credentials.indexOf(':');
                    if (colonIndex > 0) {
                        String username = credentials.substring(0, colonIndex);
                        String password = credentials.substring(colonIndex + 1);
                        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                        Authentication authResult = authenticationManager.authenticate(authRequest);
                        if (authResult.isAuthenticated()) {
                            SecurityContextHolder.getContext().setAuthentication(authResult);
                        }
                    }
                } catch (IllegalArgumentException ignored) {
                    // Invalid token; proceed without authentication
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
