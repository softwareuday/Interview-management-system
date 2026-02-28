//package com.ims.fullstack.security;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//public final class AuthUtil {
//
//    private AuthUtil() {}
//
//    public static Long getUserId() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null) return null;
//
//        Object principal = auth.getPrincipal();
//        if (principal instanceof AuthenticatedUser user) {
//            return user.getId();
//        }
//        return null;
//    }
//
//    public static String getEmail() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null) return null;
//
//        Object principal = auth.getPrincipal();
//        if (principal instanceof AuthenticatedUser user) {
//            return user.getEmail();
//        }
//        return null;
//    }
//}
package com.ims.fullstack.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUtil {
    private AuthUtil() {}

    public static AuthenticatedUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof AuthenticatedUser user) {
            return user;
        }
        return null;
    }

    public static Long getUserId() {
        AuthenticatedUser user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static String getEmail() {
        AuthenticatedUser user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }
}