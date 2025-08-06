package com.assignment.progresssoft.service;

import com.assignment.progresssoft.dto.request.BatchPermissionRequest;
import com.assignment.progresssoft.dto.request.CreateDocumentRequest;
import com.assignment.progresssoft.dto.request.GrantPermissionRequest;
import com.assignment.progresssoft.dto.response.AccessibleIdsResponse;
import com.assignment.progresssoft.dto.response.DocumentResponse;
import com.assignment.progresssoft.enums.Permissions;
import com.assignment.progresssoft.model.Document;
import com.assignment.progresssoft.model.embedded.AccessibleUser;
import com.assignment.progresssoft.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Service layer for handling business logic related to documents and permissions.
 */
@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    /**
     * Constructs a new {@code DocumentService} with the provided {@link DocumentRepository}.
     *
     * @param documentRepository the repository used for document persistence and retrieval
     */
    public DocumentService(DocumentRepository documentRepository) { this.documentRepository = documentRepository; }

    /**
     * Retrieves all documents that the specified user has READ permission for.
     *
     * @param username the username to filter documents by READ access
     * @return a stream of {@link DocumentResponse} objects
     */
    public Stream<DocumentResponse> getAllDocuments(String username) {
        return documentRepository.findAll().stream()
                .filter(document -> document.getAccessibleUsers().stream()
                        .anyMatch(user ->
                                user.getUsername().equals(username) &&
                                        user.getPermission() == Permissions.READ
                        ))
                .map(document -> new DocumentResponse(
                        document.getName(),
                        document.getContent(),
                        document.getFileType()
                ));
    }

    /**
     * Retrieves a document by its unique identifier.
     *
     * @param id the ID of the document
     * @return the {@link Document} object if found, or {@code null} if not found
     */
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }


    /**
     * Creates a new document with the provided information.
     *
     * @param createDocumentRequest the request containing document metadata and accessible users
     */
    public void createDocument(CreateDocumentRequest createDocumentRequest) {
        Document document = new Document();

        document.setName(createDocumentRequest.getName());
        document.setContent(createDocumentRequest.getContent());
        document.setFileType(createDocumentRequest.getFileType());
        document.setAccessibleUsers(createDocumentRequest.getAccessibleUsers());

        documentRepository.save(document);
    }

    /**
     * Updates or grants a new permission to a user for a given document.
     *
     * @param permissionRequest the permission to grant
     * @param document          the document to update
     */
    public void updateDocument(GrantPermissionRequest permissionRequest, Document document) {
        boolean updated = false;

        for (AccessibleUser user : document.getAccessibleUsers()) {
            if(user.getUsername().equalsIgnoreCase(permissionRequest.getUsername())) {
                user.setPermission(permissionRequest.getPermission());
                updated = true;
                break;
            }
        }

        if (!updated) {
            document.getAccessibleUsers().add(new AccessibleUser(permissionRequest.getUsername(), permissionRequest.getPermission()));
        }

        documentRepository.save(document);

    }

    /**
     * Checks which documents from a batch the specified user has the requested permission for.
     *
     * @param userName          the username of the requester (can be "admin")
     * @param permissionRequest contains the list of document IDs and the permission type
     * @return response containing a set of document IDs that the user can access
     */
    public AccessibleIdsResponse checkPermissionBatch(String userName, BatchPermissionRequest permissionRequest) {
        Set<Long> ids = userName.equals("admin") ?
                documentRepository.findDocumentIdsWithPermission(permissionRequest.getDocumentIds(),permissionRequest.getPermission()) :
                documentRepository.findDocumentIdsWithPermission(
                        permissionRequest.getDocumentIds(),
                        permissionRequest.getPermission(),
                        userName);
        return new AccessibleIdsResponse(ids);
    }

    /**
     * Deletes a document by its ID.
     *
     * @param id the ID of the document to delete
     */
    public void deleteDocument(Long id) { documentRepository.deleteById(id); }
}
