package com.assignment.progresssoft.model;


import com.assignment.progresssoft.model.embedded.AccessibleUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String content;

    @NotBlank
    private String fileType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "document_accessible_users",
            joinColumns = @JoinColumn(name = "document_id")
    )
    private Set<AccessibleUser> accessibleUsers;

    public Document() {
    }

    public Document(String name, String content, String fileType, Set<AccessibleUser> accessibleUsers) {
        this.name = name;
        this.content = content;
        this.fileType = fileType;
        this.accessibleUsers = accessibleUsers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        Document document = (Document) o;
        return Objects.equals(id, document.id) && Objects.equals(name, document.name) && Objects.equals(content, document.content) && Objects.equals(fileType, document.fileType) && Objects.equals(accessibleUsers, document.accessibleUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, content, fileType, accessibleUsers);
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", fileType='" + fileType + '\'' +
                ", accessibleUsers=" + accessibleUsers +
                '}';
    }
}
