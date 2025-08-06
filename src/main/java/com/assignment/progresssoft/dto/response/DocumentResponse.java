package com.assignment.progresssoft.dto.response;

import java.util.Objects;

public class DocumentResponse {

    private String name;

    private String content;

    private String fileType;

    public DocumentResponse() {
    }

    public DocumentResponse(String name, String content, String fileType) {
        this.name = name;
        this.content = content;
        this.fileType = fileType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentResponse that = (DocumentResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(content, that.content) && Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content, fileType);
    }

    @Override
    public String toString() {
        return "DocumentResponse{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
