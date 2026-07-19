package com.example.elvora.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor that enforces session-based authentication and
 * role-based authorization on every request.
 *
 * <ul>
 *   <li>Public paths (login, register, static resources) are always allowed.</li>
 *   <li>All other paths require a valid session with "email" and "role" attributes.</li>
 *   <li>/admin/** requires role "ADMIN".</li>
 *   <li>/user/**, /category/**, /expense/**, /budget/**, /report/** require role "USER".</li>
 * </ul>
 */
@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        // Allow public and static paths without session check
        if (isPublicPath(uri)) {
            return true;
        }

        // ── Session validation ──────────────────────────
        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("email") == null ||
                session.getAttribute("role") == null) {

            response.sendRedirect("/login");
            return false;
        }

        String role = (String) session.getAttribute("role");

        // ── Role-based access control ───────────────────

        // ADMIN-only paths
        if (uri.startsWith("/admin")) {
            if (!"ADMIN".equals(role)) {
                response.sendRedirect("/AuthError");
                return false;
            }
        }

        // USER-only paths
        if (uri.startsWith("/user") ||
                uri.startsWith("/expense") ||
                uri.startsWith("/budget") ||
                uri.startsWith("/report")) {

            if (!"USER".equals(role)) {
                response.sendRedirect("/AuthError");
                return false;
            }
        }

        // Shared paths: /category/**
        if (uri.startsWith("/category")) {
            if (!"USER".equals(role) && !"ADMIN".equals(role)) {
                response.sendRedirect("/AuthError");
                return false;
            }
        }

        return true;
    }

    /**
     * Returns {@code true} for paths that do not require authentication.
     */
    private boolean isPublicPath(String uri) {
        return uri.equals("/") ||
                uri.equals("/login") ||
                uri.equals("/logout") ||
                uri.equals("/register") ||
                uri.equals("/AuthError") ||
                uri.startsWith("/css/") ||
                uri.startsWith("/js/") ||
                uri.startsWith("/images/") ||
                uri.startsWith("/fonts/");
    }
}
