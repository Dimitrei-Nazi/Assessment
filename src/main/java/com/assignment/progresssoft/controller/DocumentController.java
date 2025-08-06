package com.assignment.progresssoft.controller;

import com.assignment.progresssoft.dto.request.BatchPermissionRequest;
import com.assignment.progresssoft.dto.request.CreateDocumentRequest;
import com.assignment.progresssoft.dto.request.GrantPermissionRequest;
import com.assignment.progresssoft.dto.response.AccessibleIdsResponse;
import com.assignment.progresssoft.dto.response.DocumentResponse;
import com.assignment.progresssoft.model.Document;
import com.assignment.progresssoft.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

/**
 * REST controller for managing documents and user permissions.
 * Supports create, retrieve, update, delete, and batch access-check operations.
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    /**
     * Constructs the controller with a {@link DocumentService}.
     *
     * @param documentService the service layer handling document operations
     */
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    /**
     * Retrieves a list of documents that the specified user has READ permission for.
     *
     * @param username the username from the X-User header
     * @return stream of {@link DocumentResponse} objects
     */
    @Operation(summary = "Get List of Documents for Username with READ permissions",
            parameters = {
                    @Parameter(
                            name = "X-User",
                            in = ParameterIn.HEADER,
                            description = "Username",
                            required = true,
                            example = "user1"
                    )
            }
    )
    @GetMapping
    public Stream<DocumentResponse> getAllUsers(
            @RequestHeader("X-User") String username) {
        return documentService.getAllDocuments(username);
    }

    /**
     * Retrieves a specific document by ID.
     *
     * @param id the document ID
     * @return {@link ResponseEntity} containing {@link DocumentResponse} or 404 if not found
     */
    @Operation(summary = "Get Document for Username with READ permissions",
            parameters = {
                    @Parameter(
                            name = "X-User",
                            in = ParameterIn.HEADER,
                            description = "Username",
                            required = true,
                            example = "user1"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        return document != null ? ResponseEntity.ok(new DocumentResponse(document.getName(), document.getContent(), document.getFileType())) : ResponseEntity.notFound().build();
    }

    /**
     * Creates a new document. Only allowed for "admin".
     *
     * @param document the document creation request
     * @return {@link ResponseEntity} with 200 OK
     */
    @Operation(summary = "Create Document admin permissions",
            parameters = {
                    @Parameter(
                            name = "X-User",
                            in = ParameterIn.HEADER,
                            description = "admin",
                            required = true,
                            example = "admin"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> createDocument(@Valid @RequestBody CreateDocumentRequest document) {
        documentService.createDocument(document);
        return ResponseEntity.ok().build();
    }

    /**
     * Grants or updates permission for a user on a document.
     * Only allowed for admin or user with WRITE access.
     *
     * @param id                the document ID
     * @param permissionRequest the permission grant/update request
     * @return {@link ResponseEntity} with 200 OK or 404 if document not found
     */
    @Operation(summary = "Update Document for admin or Username with WRITE permissions",
            parameters = {
                    @Parameter(
                            name = "X-User",
                            in = ParameterIn.HEADER,
                            description = "Username or admin",
                            required = true,
                            example = "admin"
                    )
            }
    )
    @PostMapping("/{id}/grant")
    public ResponseEntity<Void>  updateDocument(@PathVariable Long id, @Valid @RequestBody GrantPermissionRequest permissionRequest) {
        Document document = documentService.getDocumentById(id);
        if(document != null ) {
            documentService.updateDocument(permissionRequest, document);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

     /**
     * Checks which documents a user or admin has specific permission for.
     *
     * @param userName          the username from header
     * @param permissionRequest the permission request with document IDs
     * @return response with list of accessible document IDs
     */
    @Operation(summary = "Batch Permission Check for admin or Username with requested permissions",
            parameters = {
                    @Parameter(
                            name = "X-User",
                            in = ParameterIn.HEADER,
                            description = "Username or admin",
                            required = true,
                            example = "admin"
                    )
            }
    )
    @PostMapping("/access-check")
    public AccessibleIdsResponse batchPermissionCheck(@RequestHeader("X-User") String userName, @Valid @RequestBody BatchPermissionRequest permissionRequest) {
        return documentService.checkPermissionBatch(userName, permissionRequest);
    }

    /**
     * Deletes a document. Only allowed if user has DELETE permissions.
     *
     * @param id the document ID
     * @return {@link ResponseEntity} with 204 No Content or 404 if document not found
     */
    @Operation(summary = "Delete Document for Username with DELETE permissions",
            parameters = {
                    @Parameter(
                            name = "X-User",
                            in = ParameterIn.HEADER,
                            description = "Username",
                            required = true,
                            example = "user1"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        if(document != null ) {
            documentService.deleteDocument(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
