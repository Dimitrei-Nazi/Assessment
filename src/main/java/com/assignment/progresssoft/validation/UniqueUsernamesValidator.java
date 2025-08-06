package com.assignment.progresssoft.validation;

import com.assignment.progresssoft.dto.request.CreateDocumentRequest;
import com.assignment.progresssoft.model.embedded.AccessibleUser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class UniqueUsernamesValidator implements ConstraintValidator<UniqueUsernames, CreateDocumentRequest> {

    @Override
    public boolean isValid(CreateDocumentRequest value, ConstraintValidatorContext context) {
        if (value == null || value.getAccessibleUsers() == null) {
            return true;
        }

        Set<String> seenUsernames = new HashSet<>();
        for (AccessibleUser user : value.getAccessibleUsers()) {
            if (user.getUsername().isBlank()) {
                return true;
            }
            if (!seenUsernames.add(user.getUsername())) {
                return false;
            }
        }

        return true;
    }
}
