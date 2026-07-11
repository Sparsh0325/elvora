package com.example.elvora.repository;

import com.example.elvora.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository
        extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.created_by = :email")
    List<Category> findByCreatedBy(
            @Param("email") String email
    );

    @Query("""
            SELECT c FROM Category c
            WHERE c.created_by = 'admin'
            OR c.created_by = :email
            """)
    List<Category> findAvailableCategories(
            @Param("email") String email
    );
}