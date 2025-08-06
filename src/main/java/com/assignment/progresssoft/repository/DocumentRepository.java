package com.assignment.progresssoft.repository;

import com.assignment.progresssoft.enums.Permissions;
import com.assignment.progresssoft.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("""
    SELECT d.id
    FROM Document d
    JOIN d.accessibleUsers au
    WHERE d.id IN :ids
      AND au.permission = :permission
    """)
    Set<Long> findDocumentIdsWithPermission(@Param("ids") Set<Long> ids,
                                             @Param("permission") Permissions permission);

    @Query("""
    SELECT d.id
    FROM Document d
    JOIN d.accessibleUsers au
    WHERE d.id IN :ids
      AND au.permission = :permission
      AND au.username = :username
    """)
    Set<Long> findDocumentIdsWithPermission(
            @Param("ids") Set<Long> ids,
            @Param("permission") Permissions permission,
            @Param("username") String username
    );
}
