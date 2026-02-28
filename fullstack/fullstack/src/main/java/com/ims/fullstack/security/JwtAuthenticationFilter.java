////package com.ims.fullstack.security;
////
////import io.jsonwebtoken.Claims;
////import jakarta.servlet.FilterChain;
////import jakarta.servlet.ServletException;
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.servlet.http.HttpServletResponse;
////import org.springframework.http.HttpHeaders;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.stereotype.Component;
////import org.springframework.web.filter.OncePerRequestFilter;
////
////import java.io.IOException;
////import java.util.List;
////
/////**
//// * JWT filter with detailed debug logging to trace request flow and token parsing.
//// */
////@Component
////public class JwtAuthenticationFilter extends OncePerRequestFilter {
////
////    private final JwtService jwtService;
////
////    public JwtAuthenticationFilter(JwtService jwtService) {
////        this.jwtService = jwtService;
////    }
////
////    @Override
////    protected void doFilterInternal(HttpServletRequest request,
////                                    HttpServletResponse response,
////                                    FilterChain filterChain)
////            throws ServletException, IOException {
////
////        String path = request.getRequestURI();
////        try {
////            // Quick debug header
////            System.out.println("---- REQUEST START ------------------------------------------------");
////            System.out.println("Incoming: " + request.getMethod() + " " + path);
////            System.out.println("Remote Addr: " + request.getRemoteAddr());
////
////            // Skip public endpoints (login/register/etc.)
////            if (path != null && (path.startsWith("/api/auth/") || path.equals("/") || path.contains("/error"))) {
////                System.out.println("🟡 Skipping JWT validation for public endpoint: " + path);
////                filterChain.doFilter(request, response);
////                System.out.println("---- REQUEST END (public) -----------------------------------------");
////                return;
////            }
////
////            // Read Authorization header
////            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
////            System.out.println("Authorization Header (raw): " + authHeader);
////
////            String token = null;
////            if (authHeader != null && !authHeader.isBlank()) {
////                String h = authHeader.trim();
////                if (h.toLowerCase().startsWith("bearer ")) {
////                    token = h.substring(7).trim();
////                } else {
////                    token = h;
////                }
////            }
////
////            System.out.println("Extracted token: " + (token == null ? "null" : token.substring(0, Math.min(60, token.length())) + (token.length() > 60 ? "..." : "")));
////
////            if (token == null || token.isBlank()) {
////                System.out.println("⚠️ No JWT token provided. Proceeding without authentication (security will handle access).");
////                filterChain.doFilter(request, response);
////                System.out.println("---- REQUEST END (no token) --------------------------------------");
////                return;
////            }
////
////            // Validate token with jwtService
////            boolean valid;
////            try {
////                valid = jwtService.isTokenValid(token);
////            } catch (Exception ex) {
////                System.err.println("❌ Exception while validating token: " + ex.getMessage());
////                ex.printStackTrace();
////                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token (validation error)");
////                return;
////            }
////
////            if (!valid) {
////                System.out.println("❌ JWT validation returned false.");
////                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
////                return;
////            }
////
////            // Parse claims
////            Claims claims;
////            try {
////                claims = jwtService.getClaims(token);
////            } catch (Exception ex) {
////                System.err.println("❌ Failed to parse JWT claims: " + ex.getMessage());
////                ex.printStackTrace();
////                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token (claims error)");
////                return;
////            }
////
////            String email = claims.getSubject();
////            String rawRole = claims.get("role", String.class);
////            System.out.println("Claims -> subject: " + email + ", role: " + rawRole + ", expiresAt: " + claims.getExpiration());
////
////            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
////
////                String normalizedRole = (rawRole == null) ? "ROLE_RECRUITER" : rawRole;
////                if (!normalizedRole.startsWith("ROLE_")) {
////                    normalizedRole = "ROLE_" + normalizedRole;
////                }
////
////                UsernamePasswordAuthenticationToken authToken =
////                        new UsernamePasswordAuthenticationToken(
////                                email,
////                                null,
////                                List.of(new SimpleGrantedAuthority(normalizedRole))
////                        );
////
////                SecurityContextHolder.getContext().setAuthentication(authToken);
////                System.out.println("✅ JWT authentication SET for: " + email);
////            } else {
////                System.out.println("⚠️ Authentication already exists — skipping overwrite");
////            }
////
////
////
////            filterChain.doFilter(request, response);
////            System.out.println("---- REQUEST END --------------------------------------------------");
////
////        } catch (Exception e) {
////            System.err.println("🔥 Exception in JwtAuthenticationFilter: " + e.getMessage());
////            e.printStackTrace();
////            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT Filter Error");
////        }
////    }
////}
//
//
//package com.ims.fullstack.security;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * JWT Authentication Filter
// * ✅ FIXED: Attaches authenticated user (id + email + role) to SecurityContext
// */
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//
//    public JwtAuthenticationFilter(JwtService jwtService) {
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//
//        try {
//            System.out.println("---- REQUEST START ------------------------------------------------");
//            System.out.println("Incoming: " + request.getMethod() + " " + path);
//
//            // ✅ Skip public endpoints
//            if (path.startsWith("/api/auth/") || path.equals("/") || path.contains("/error")) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//            if (authHeader == null || authHeader.isBlank()) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            String token = authHeader.toLowerCase().startsWith("bearer ")
//                    ? authHeader.substring(7).trim()
//                    : authHeader.trim();
//
//            if (!jwtService.isTokenValid(token)) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//                return;
//            }
//
//            Claims claims = jwtService.getClaims(token);
//
//            String email = claims.getSubject();
//            Long userId = claims.get("id", Long.class);
//            String role = claims.get("role", String.class);
//
//            if (email == null || userId == null) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT claims");
//                return;
//            }
//
//            String normalizedRole = role != null && role.startsWith("ROLE_")
//                    ? role
//                    : "ROLE_RECRUITER";
//
//            // ✅ CRITICAL FIX: use real principal instead of email string
//            AuthenticatedUser principal =
//                    new AuthenticatedUser(userId, email, normalizedRole);
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            principal,
//                            null,
//                            List.of(new SimpleGrantedAuthority(normalizedRole))
//                    );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            System.out.println("✅ Authenticated user: " + email + " (id=" + userId + ")");
//
//            filterChain.doFilter(request, response);
//            System.out.println("---- REQUEST END --------------------------------------------------");
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response.sendError(
//                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                    "JWT Authentication Error"
//            );
//        }
//    }
//}






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
