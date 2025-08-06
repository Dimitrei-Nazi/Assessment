package com.assignment.progresssoft.security.service;

import com.assignment.progresssoft.enums.Permissions;
import com.assignment.progresssoft.model.Document;
import com.assignment.progresssoft.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for checking user permissions on documents.
 * <p>
 * Provides methods to verify if a user has create, read, write, or delete
 * permissions on specific documents.
 */
@Service
public class PermissionCheckingService {
    private final DocumentRepository documentRepository;

    /**
     * Constructs the {@code PermissionCheckingService} with the required
     * {@link DocumentRepository}.
     *
     * @param documentRepository the repository used to fetch documents and their permissions
     */
    public PermissionCheckingService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Checks if the given user has permission to create new documents.
     * <p>
     * In this implementation, only users with username "admin" have create permissions.
     *
     * @param username the username of the user to check
     * @return {@code true} if the user can create new documents; {@code false} otherwise
     */
    public boolean hasCreateNewPermission(String username) {
        return "admin".equalsIgnoreCase(username);
    }

    /**
     * Checks if the user has write permission on a specific document.
     *
     * @param username   the username of the user
     * @param documentId the ID of the document to check
     * @return {@code true} if the user is "admin" or has WRITE permission on the document;
     *         {@code false} if the document does not exist or user lacks permission
     */
    public boolean hasWritePermission(String username, Long documentId) {
        if ("admin".equalsIgnoreCase(username)) {
            return true;
        }

        Optional<Document> optDoc = documentRepository.findById(documentId);
        if (optDoc.isEmpty()) {
            return false;
        }

        Document document = optDoc.get();

        return document.getAccessibleUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPermission() == Permissions.WRITE);
    }

    /**
     * Checks if the user has read permission on a specific document.
     *
     * @param username   the username of the user
     * @param documentId the ID of the document to check
     * @return {@code true} if the user has READ permission on the document;
     *         {@code false} if the document does not exist or user lacks permission
     */
    public boolean hasReadPermission(String username, Long documentId) {
        Optional<Document> optDoc = documentRepository.findById(documentId);
        if (optDoc.isEmpty()) {
            return false;
        }

        Document document = optDoc.get();

        return document.getAccessibleUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPermission() == Permissions.READ);
    }

    /**
     * Checks if the user has delete permission on a specific document.
     *
     * @param username   the username of the user
     * @param documentId the ID of the document to check
     * @return {@code true} if the user has DELETE permission on the document;
     *         {@code false} if the document does not exist or user lacks permission
     */
    public boolean hasDeletePermission(String username, Long documentId) {
        Optional<Document> optDoc = documentRepository.findById(documentId);
        if (optDoc.isEmpty()) {
            return false;
        }

        Document document = optDoc.get();

        return document.getAccessibleUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPermission() == Permissions.DELETE);
    }
}
