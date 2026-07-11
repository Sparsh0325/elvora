package com.example.elvora.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "categorydata")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_id;

    private String category_name;

    private String created_by;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Category() {
    }

    public Category(int category_id, String category_name, String created_by) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.created_by = created_by;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                ", created_by='" + created_by + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}