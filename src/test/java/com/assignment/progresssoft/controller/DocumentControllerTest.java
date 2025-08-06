package com.assignment.progresssoft.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import com.assignment.progresssoft.dto.request.BatchPermissionRequest;
import com.assignment.progresssoft.dto.request.CreateDocumentRequest;
import com.assignment.progresssoft.dto.request.GrantPermissionRequest;
import com.assignment.progresssoft.dto.response.AccessibleIdsResponse;
import com.assignment.progresssoft.dto.response.DocumentResponse;
import com.assignment.progresssoft.model.Document;
import com.assignment.progresssoft.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

public class DocumentControllerTest {

    private DocumentService documentService;
    private DocumentController controller;

    @BeforeEach
    public void setup() {
        documentService = mock(DocumentService.class);
        controller = new DocumentController(documentService);
    }

    @Test
    public void testGetAllUsers() {
        String username = "user1";
        DocumentResponse doc1 = new DocumentResponse("Doc1", "Content1", "txt");
        DocumentResponse doc2 = new DocumentResponse("Doc2", "Content2", "pdf");

        when(documentService.getAllDocuments(username)).thenReturn(Stream.of(doc1, doc2));

        Stream<DocumentResponse> response = controller.getAllUsers(username);

        assertNotNull(response);
        assertEquals(2, response.count());
        verify(documentService).getAllDocuments(username);
    }

    @Test
    public void testGetDocumentById_found() {
        Long id = 1L;
        Document doc = new Document();
        doc.setName("Doc1");
        doc.setContent("Content1");
        doc.setFileType("txt");

        when(documentService.getDocumentById(id)).thenReturn(doc);

        ResponseEntity<DocumentResponse> response = controller.getDocumentById(id);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Doc1", response.getBody().getName());
        verify(documentService).getDocumentById(id);
    }

    @Test
    public void testGetDocumentById_notFound() {
        Long id = 1L;
        when(documentService.getDocumentById(id)).thenReturn(null);

        ResponseEntity<DocumentResponse> response = controller.getDocumentById(id);

        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
        verify(documentService).getDocumentById(id);
    }

    @Test
    public void testCreateDocument() {
        CreateDocumentRequest request = mock(CreateDocumentRequest.class);

        ResponseEntity<Void> response = controller.createDocument(request);

        assertEquals(OK, response.getStatusCode());
        verify(documentService).createDocument(request);
    }

    @Test
    public void testUpdateDocument_found() {
        Long id = 1L;
        GrantPermissionRequest request = mock(GrantPermissionRequest.class);
        Document doc = new Document();

        when(documentService.getDocumentById(id)).thenReturn(doc);

        ResponseEntity<Void> response = controller.updateDocument(id, request);

        assertEquals(OK, response.getStatusCode());
        verify(documentService).getDocumentById(id);
        verify(documentService).updateDocument(request, doc);
    }

    @Test
    public void testUpdateDocument_notFound() {
        Long id = 1L;
        GrantPermissionRequest request = mock(GrantPermissionRequest.class);

        when(documentService.getDocumentById(id)).thenReturn(null);

        ResponseEntity<Void> response = controller.updateDocument(id, request);

        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
        verify(documentService).getDocumentById(id);
        verify(documentService, never()).updateDocument(any(), any());
    }

    @Test
    public void testBatchPermissionCheck() {
        String username = "user1";
        BatchPermissionRequest request = mock(BatchPermissionRequest.class);
        AccessibleIdsResponse responseMock = mock(AccessibleIdsResponse.class);

        when(documentService.checkPermissionBatch(username, request)).thenReturn(responseMock);

        AccessibleIdsResponse response = controller.batchPermissionCheck(username, request);

        assertNotNull(response);
        assertEquals(responseMock, response);
        verify(documentService).checkPermissionBatch(username, request);
    }

    @Test
    public void testDeleteUser_found() {
        Long id = 1L;
        Document doc = new Document();

        when(documentService.getDocumentById(id)).thenReturn(doc);

        ResponseEntity<Void> response = controller.deleteUser(id);

        assertEquals(204, response.getStatusCodeValue());

        verify(documentService).getDocumentById(id);
        verify(documentService).deleteDocument(id);
    }

    @Test
    public void testDeleteUser_notFound() {
        Long id = 1L;

        when(documentService.getDocumentById(id)).thenReturn(null);

        ResponseEntity<Void> response = controller.deleteUser(id);

        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
        verify(documentService).getDocumentById(id);
        verify(documentService, never()).deleteDocument(anyLong());
    }
}
