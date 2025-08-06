package com.assignment.progresssoft.dto.request;

import com.assignment.progresssoft.enums.Permissions;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;

import java.util.Objects;
import java.util.Set;

public class BatchPermissionRequest {

    @Enumerated(EnumType.STRING)
    private Permissions permission;

    private Set<@Valid Long> documentIds;

    public BatchPermissionRequest() {
    }

    public BatchPermissionRequest(Permissions permission, Set<Long> documentIds) {
        this.permission = permission;
        this.documentIds = documentIds;
    }

    public Permissions getPermission() {
        return permission;
    }

    public void setPermission(Permissions permission) {
        this.permission = permission;
    }

    public Set<Long> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(Set<Long> documentIds) {
        this.documentIds = documentIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchPermissionRequest that = (BatchPermissionRequest) o;
        return permission == that.permission && Objects.equals(documentIds, that.documentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permission, documentIds);
    }

    @Override
    public String toString() {
        return "BatchPermissionRequest{" +
                "permission=" + permission +
                ", documentIds=" + documentIds +
                '}';
    }
}
