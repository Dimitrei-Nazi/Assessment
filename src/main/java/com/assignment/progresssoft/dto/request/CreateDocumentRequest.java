package com.assignment.progresssoft.dto.request;

import com.assignment.progresssoft.model.embedded.AccessibleUser;
import com.assignment.progresssoft.validation.UniqueUsernames;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;
import java.util.Set;

@UniqueUsernames
public class CreateDocumentRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotBlank(message = "File Type cannot be empty")
    private String fileType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "document_accessible_users",
            joinColumns = @JoinColumn(name = "document_id")
    )
    private Set<@Valid AccessibleUser> accessibleUsers;

    public CreateDocumentRequest() {
    }

    public CreateDocumentRequest(String name, String content, String fileType, Set<AccessibleUser> accessibleUsers) {
        this.name = name;
        this.content = content;
        this.fileType = fileType;
        this.accessibleUsers = accessibleUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Set<AccessibleUser> getAccessibleUsers() {
        return accessibleUsers;
    }

    public void setAccessibleUsers(Set<AccessibleUser> accessibleUsers) {
        this.accessibleUsers = accessibleUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateDocumentRequest that = (CreateDocumentRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(content, that.content) && Objects.equals(fileType, that.fileType) && Objects.equals(accessibleUsers, that.accessibleUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content, fileType, accessibleUsers);
    }

    @Override
    public String toString() {
        return "CreateDocumentRequest{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", fileType='" + fileType + '\'' +
                ", accessibleUsers=" + accessibleUsers +
                '}';
    }
}
