






package com.ims.fullstack.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        try {
            if (path.startsWith("/api/auth/") || path.equals("/") || path.contains("/error")) {
                filterChain.doFilter(request, response);
                return;
            }
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || authHeader.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authHeader.toLowerCase().startsWith("bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();
            if (!jwtService.isTokenValid(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
            Claims claims = jwtService.getClaims(token);
            String email = claims.getSubject();
            Long userId = claims.get("id", Long.class);
            String role = claims.get("role", String.class);
            if (email == null || userId == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT claims");
                return;
            }
            String normalizedRole = role != null && role.startsWith("ROLE_") ? role : "ROLE_" + role;
            AuthenticatedUser principal = new AuthenticatedUser(userId, email, normalizedRole);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, null,
                            List.of(new SimpleGrantedAuthority(normalizedRole)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT Authentication Error");
        }
    }
}
