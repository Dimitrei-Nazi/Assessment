package com.assignment.progresssoft.dto.request;

import com.assignment.progresssoft.enums.Permissions;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class GrantPermissionRequest {

    @NotBlank
    private String username;

    @Enumerated(EnumType.STRING)
    private Permissions permission;

    public GrantPermissionRequest() {
    }

    public GrantPermissionRequest(String username, Permissions permission) {
        this.username = username;
        this.permission = permission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Permissions getPermission() {
        return permission;
    }

    public void setPermission(Permissions permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrantPermissionRequest that = (GrantPermissionRequest) o;
        return Objects.equals(username, that.username) && permission == that.permission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, permission);
    }

    @Override
    public String toString() {
        return "GrantPermissionRequest{" +
                "username='" + username + '\'' +
                ", permission=" + permission +
                '}';
    }
}
