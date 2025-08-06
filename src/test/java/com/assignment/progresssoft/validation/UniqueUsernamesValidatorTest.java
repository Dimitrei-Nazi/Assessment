package com.assignment.progresssoft.validation;

import com.assignment.progresssoft.dto.request.CreateDocumentRequest;
import com.assignment.progresssoft.model.embedded.AccessibleUser;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class UniqueUsernamesValidatorTest {

    private UniqueUsernamesValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setup() {
        validator = new UniqueUsernamesValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void testValidUniqueUsernames() {
        CreateDocumentRequest request = new CreateDocumentRequest();
        request.setAccessibleUsers(Set.of(
                new AccessibleUser("user1", null),
                new AccessibleUser("user2", null)
        ));

        assertTrue(validator.isValid(request, context));
    }

    @Test
    void testUsernameIsBlank_returnsTrue() {
        CreateDocumentRequest request = new CreateDocumentRequest();
        request.setAccessibleUsers(Set.of(
                new AccessibleUser("", null),
                new AccessibleUser("user2", null)
        ));

        assertTrue(validator.isValid(request, context));
    }

    @Test
    void testNullAccessibleUsers_returnsTrue() {
        CreateDocumentRequest request = new CreateDocumentRequest();
        request.setAccessibleUsers(null);

        assertTrue(validator.isValid(request, context));
    }

    @Test
    void testNullRequest_returnsTrue() {
        assertTrue(validator.isValid(null, context));
    }
}
