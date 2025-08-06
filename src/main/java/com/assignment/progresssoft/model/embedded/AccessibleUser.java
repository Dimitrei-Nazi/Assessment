package com.assignment.progresssoft.model.embedded;

import com.assignment.progresssoft.enums.Permissions;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class AccessibleUser {

    @NotBlank(message = "User Name cannot be empty")
    private String username;

    @Enumerated(EnumType.STRING)
    private Permissions permission;

    public AccessibleUser() {
    }

    public AccessibleUser(String username, Permissions permission) {
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
        AccessibleUser that = (AccessibleUser) o;
        return Objects.equals(username, that.username) && permission == that.permission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, permission);
    }

    @Override
    public String toString() {
        return "AccessibleUser{" +
                "username='" + username + '\'' +
                ", permission=" + permission +
                '}';
    }
}
