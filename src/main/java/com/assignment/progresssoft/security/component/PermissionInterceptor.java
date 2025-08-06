package com.assignment.progresssoft.security.component;

import com.assignment.progresssoft.security.service.PermissionCheckingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Intercepts incoming HTTP requests to perform permission checks based on the user's role and access rights.
 * <p>
 * This interceptor enforces security policies for various document-related actions by examining
 * the HTTP method, request URI, and the {@code X-User} header provided in the request.
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private final PermissionCheckingService permissionCheckingService;

    /**
     * Constructs a {@code PermissionInterceptor} with the provided permission checking service.
     *
     * @param permissionCheckingService the service responsible for validating user permissions
     */
    public PermissionInterceptor(PermissionCheckingService permissionCheckingService) {
        this.permissionCheckingService = permissionCheckingService;
    }

    /**
     * Intercepts HTTP requests before they reach the controller layer and performs access control checks.
     *
     * @param request  the incoming HTTP request
     * @param response the HTTP response
     * @param handler  the chosen handler to execute
     * @return {@code true} if the request should proceed, {@code false} if access is denied or the request is malformed
     * @throws Exception if an error occurs while processing
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String username = request.getHeader("X-User");
        if (username == null || username.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing X-User header");
            return false;
        }

        String method = request.getMethod();
        String path = request.getRequestURI();

        // DELETE: Requires DELETE permission for specific document
        if ("DELETE".equalsIgnoreCase(method) && path.matches("^/documents/\\d+")) {
            Long documentId = extractDocumentId(path, response);
            if (documentId == null) return false;

            if (!permissionCheckingService.hasDeletePermission(username, documentId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User does not have DELETE permission");
                return false;
            }
        }

        // POST /{id}/grant: Requires WRITE permission
        if ("POST".equalsIgnoreCase(method) && path.matches("^/documents/\\d+/grant$")) {
            Long documentId = extractDocumentId(path, response);
            if (documentId == null) return false;

            if (!permissionCheckingService.hasWritePermission(username, documentId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User does not have WRITE permission");
                return false;
            }
        }

        // POST /documents: Requires CREATE permission
        if ("POST".equalsIgnoreCase(method) && path.matches("^/documents$")) {
            if (!permissionCheckingService.hasCreateNewPermission(username)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User does not have WRITE permission");
                return false;
            }
        }

        // GET /documents: Skip check (filter handled in service layer)
        if ("GET".equalsIgnoreCase(method) && path.matches("^/documents$")) {
            return true;
        }

        // GET /documents/{id}: Requires READ permission
        if ("GET".equalsIgnoreCase(method) && path.matches("^/documents/\\d+")) {
            Long documentId = extractDocumentId(path, response);
            if (documentId == null) return false;

            if (!permissionCheckingService.hasReadPermission(username, documentId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User does not have READ permission");
                return false;
            }
        }

        return true;
    }

    /**
     * Helper method to extract a document ID from the request path.
     *
     * @param path     the request URI
     * @param response the HTTP response, used to report errors
     * @return the document ID if valid, or {@code null} if invalid (error already sent)
     * @throws Exception if ID parsing fails
     */
    private Long extractDocumentId(String path, HttpServletResponse response) throws Exception {
        try {
            String[] parts = path.split("/");
            return Long.parseLong(parts[2]);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid document id");
            return null;
        }
    }
}

