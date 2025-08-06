package com.assignment.progresssoft.service;

import com.assignment.progresssoft.dto.request.BatchPermissionRequest;
import com.assignment.progresssoft.dto.request.CreateDocumentRequest;
import com.assignment.progresssoft.dto.request.GrantPermissionRequest;
import com.assignment.progresssoft.dto.response.AccessibleIdsResponse;
import com.assignment.progresssoft.enums.Permissions;
import com.assignment.progresssoft.model.Document;
import com.assignment.progresssoft.model.embedded.AccessibleUser;
import com.assignment.progresssoft.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    private DocumentRepository documentRepository;
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        documentRepository = mock(DocumentRepository.class);
        documentService = new DocumentService(documentRepository);
    }

    @Test
    void testGetAllDocuments_WithReadPermission() {
        AccessibleUser user = new AccessibleUser("user1", Permissions.READ);
        Document doc = new Document("Test", "Content", "txt", Set.of(user));
        when(documentRepository.findAll()).thenReturn(List.of(doc));

        var result = documentService.getAllDocuments("user1").toList();

        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getName());
    }

    @Test
    void testGetAllDocuments_NoPermission() {
        AccessibleUser user = new AccessibleUser("user1", Permissions.WRITE);
        Document doc = new Document("Test", "Content", "txt", Set.of(user));
        when(documentRepository.findAll()).thenReturn(List.of(doc));

        var result = documentService.getAllDocuments("user1").toList();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDocumentById_Found() {
        Document doc = new Document();
        doc.setId(1L);
        when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));

        Document result = documentService.getDocumentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetDocumentById_NotFound() {
        when(documentRepository.findById(2L)).thenReturn(Optional.empty());

        Document result = documentService.getDocumentById(2L);

        assertNull(result);
    }

    @Test
    void testCreateDocument() {
        CreateDocumentRequest request = new CreateDocumentRequest("Doc", "Content", "pdf", Set.of());
        documentService.createDocument(request);

        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository).save(captor.capture());

        assertEquals("Doc", captor.getValue().getName());
    }

    @Test
    void testUpdateDocument_UpdateExistingUser() {
        AccessibleUser user = new AccessibleUser("user1", Permissions.READ);
        Document document = new Document("Doc", "Content", "pdf", new HashSet<>(Set.of(user)));
        GrantPermissionRequest req = new GrantPermissionRequest("user1", Permissions.WRITE);

        documentService.updateDocument(req, document);

        assertEquals(Permissions.WRITE, user.getPermission());
        verify(documentRepository).save(document);
    }

    @Test
    void testUpdateDocument_AddNewUser() {
        Document document = new Document("Doc", "Content", "pdf", new HashSet<>());
        GrantPermissionRequest req = new GrantPermissionRequest("user1", Permissions.READ);

        documentService.updateDocument(req, document);

        assertEquals(1, document.getAccessibleUsers().size());
        verify(documentRepository).save(document);
    }

    @Test
    void testCheckPermissionBatch_Admin() {
        Set<Long> mockIds = Set.of(1L, 2L);
        BatchPermissionRequest req = new BatchPermissionRequest(Permissions.READ, mockIds);
        when(documentRepository.findDocumentIdsWithPermission(mockIds, Permissions.READ)).thenReturn(mockIds);

        AccessibleIdsResponse response = documentService.checkPermissionBatch("admin", req);

        assertEquals(mockIds, response.getAccessibleIds());
    }

    @Test
    void testCheckPermissionBatch_User() {
        Set<Long> mockIds = Set.of(1L);
        BatchPermissionRequest req = new BatchPermissionRequest(Permissions.READ, Set.of(1L, 2L));
        when(documentRepository.findDocumentIdsWithPermission(Set.of(1L, 2L), Permissions.READ, "user1")).thenReturn(mockIds);

        AccessibleIdsResponse response = documentService.checkPermissionBatch("user1", req);

        assertEquals(mockIds, response.getAccessibleIds());
    }

    @Test
    void testDeleteDocument() {
        documentService.deleteDocument(1L);
        verify(documentRepository).deleteById(1L);
    }
}

