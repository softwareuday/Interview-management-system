////package com.ims.fullstack.security;
////
////import io.jsonwebtoken.*;
////import io.jsonwebtoken.io.Decoders;
////import io.jsonwebtoken.security.Keys;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.stereotype.Component;
////
////import java.security.Key;
////import java.util.Date;
////
////@Component
////public class JwtService {
////
////    @Value("${jwt.secret}")
////    private String secret;
////
////    @Value("${jwt.expiration-ms:86400000}")
////    private long expirationMs;
////
////    private Key getSignKey() {
////        try {
////            // Try base64 decode first
////            byte[] keyBytes = Decoders.BASE64.decode(secret);
////            return Keys.hmacShaKeyFor(keyBytes);
////        } catch (Exception e) {
////            // Fallback: treat as plain text secret
////            System.out.println("⚠️ Using plain text secret (not Base64)");
////            return Keys.hmacShaKeyFor(secret.getBytes());
////        }
////    }
////
////    public String generateToken(String email, Long id, String role) {
////        Date now = new Date();
////        Date expiry = new Date(now.getTime() + expirationMs);
////        return Jwts.builder()
////                .setSubject(email)
////                .claim("id", id)
////                .claim("role", role)
////                .setIssuedAt(now)
////                .setExpiration(expiry)
////                .signWith(getSignKey(), SignatureAlgorithm.HS256)
////                .compact();
////    }
////
////    public boolean isTokenValid(String token) {
////        try {
////            Jwts.parserBuilder()
////                    .setSigningKey(getSignKey())
////                    .build()
////                    .parseClaimsJws(token);
////            return true;
////        } catch (JwtException | IllegalArgumentException ex) {
////            System.out.println("❌ JWT validation failed: " + ex.getMessage());
////            return false;
////        }
////    }
////
////    public Claims getClaims(String token) {
////        return Jwts.parserBuilder()
////                .setSigningKey(getSignKey())
////                .build()
////                .parseClaimsJws(token)
////                .getBody();
////    }
////}
//
//
////package com.ims.fullstack.security;
////
////import io.jsonwebtoken.*;
////import io.jsonwebtoken.io.Decoders;
////import io.jsonwebtoken.security.Keys;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.stereotype.Component;
////
////import java.security.Key;
////import java.util.Date;
////
////@Component
////public class JwtService {
////
////    @Value("${jwt.secret}")
////    private String secret;
////
////    @Value("${jwt.expiration-ms:86400000}")
////    private long expirationMs;
////
////    private Key getSignKey() {
////        try {
////            // Try base64 decode first
////            byte[] keyBytes = Decoders.BASE64.decode(secret);
////            return Keys.hmacShaKeyFor(keyBytes);
////        } catch (Exception e) {
////            // Fallback: treat as plain text secret
////            System.out.println("⚠️ Using plain text secret (not Base64)");
////            return Keys.hmacShaKeyFor(secret.getBytes());
////        }
////    }
////
////    // ✅ TOKEN GENERATION (UNCHANGED)
////    public String generateToken(String email, Long id, String role) {
////        Date now = new Date();
////        Date expiry = new Date(now.getTime() + expirationMs);
////
////        return Jwts.builder()
////                .setSubject(email) // 👈 email stored as subject
////                .claim("id", id)   // 👈 candidate/recruiter id
////                .claim("role", role)
////                .setIssuedAt(now)
////                .setExpiration(expiry)
////                .signWith(getSignKey(), SignatureAlgorithm.HS256)
////                .compact();
////    }
////
////    // ✅ TOKEN VALIDATION (UNCHANGED)
////    public boolean isTokenValid(String token) {
////        try {
////            Jwts.parserBuilder()
////                    .setSigningKey(getSignKey())
////                    .build()
////                    .parseClaimsJws(token);
////            return true;
////        } catch (JwtException | IllegalArgumentException ex) {
////            System.out.println("❌ JWT validation failed: " + ex.getMessage());
////            return false;
////        }
////    }
////
////    // ✅ CLAIM EXTRACTION (UNCHANGED)
////    public Claims getClaims(String token) {
////        return Jwts.parserBuilder()
////                .setSigningKey(getSignKey())
////                .build()
////                .parseClaimsJws(token)
////                .getBody();
////    }
////
////    // ======================================================
////    // 🆕 ADDED METHODS (THIS FIXES YOUR ERRORS)
////    // ======================================================
////
////    // ✅ Used by controllers & security filters
////    public String extractUsername(String token) {
////        return getClaims(token).getSubject();
////    }
////
////    // ✅ Used for resume upload & application linking
////    public Long extractId(String token) {
////        Object id = getClaims(token).get("id");
////        if (id instanceof Integer) {
////            return ((Integer) id).longValue();
////        }
////        return (Long) id;
////    }
////
////    // ✅ Optional (future use)
////    public String extractRole(String token) {
////        return (String) getClaims(token).get("role");
////    }
////}
//
//package com.ims.fullstack.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtService {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration-ms:86400000}")
//    private long expirationMs;
//
//    // =========================
//    // 🔐 SIGNING KEY
//    // =========================
//    private Key getSignKey() {
//        try {
//            byte[] keyBytes = Decoders.BASE64.decode(secret);
//            return Keys.hmacShaKeyFor(keyBytes);
//        } catch (Exception e) {
//            // fallback if secret is plain text
//            return Keys.hmacShaKeyFor(secret.getBytes());
//        }
//    }
//
//    // =========================
//    // 🎟️ TOKEN GENERATION
//    // =========================
//    public String generateToken(String email, Long id, String role) {
//        Date now = new Date();
//        Date expiry = new Date(now.getTime() + expirationMs);
//
//        return Jwts.builder()
//                .setSubject(email)          // email
//                .claim("id", id)            // user id (candidate/recruiter)
//                .claim("role", role)        // ROLE_CANDIDATE / ROLE_RECRUITER
//                .setIssuedAt(now)
//                .setExpiration(expiry)
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // =========================
//    // ✅ TOKEN VALIDATION
//    // =========================
//    public boolean isTokenValid(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getSignKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException ex) {
//            return false;
//        }
//    }
//
//    // =========================
//    // 📦 CLAIM EXTRACTION
//    // =========================
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//    // ✅ Backward compatibility for JwtAuthenticationFilter
//    public Claims getClaims(String token) {
//        return extractAllClaims(token);
//    }
//
//
//    // 🔑 EMAIL (subject)
//    public String extractUsername(String token) {
//        return extractAllClaims(token).getSubject();
//    }
//
//    // 🔑 USER ID (candidate / recruiter)
//    public Long extractId(String token) {
//        Object id = extractAllClaims(token).get("id");
//        if (id instanceof Integer) {
//            return ((Integer) id).longValue();
//        }
//        return (Long) id;
//    }
//
//    // 🔑 ROLE
//    public String extractRole(String token) {
//        return (String) extractAllClaims(token).get("role");
//    }
//}


package com.ims.fullstack.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

    private Key getSignKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    public String generateToken(String email, Long id, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    public Claims getClaims(String token) {
        return extractAllClaims(token);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractId(String token) {
        Object id = extractAllClaims(token).get("id");
        if (id instanceof Integer) return ((Integer) id).longValue();
        return (Long) id;
    }

    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }
}