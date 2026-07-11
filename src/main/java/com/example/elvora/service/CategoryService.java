package com.example.elvora.service;

import com.example.elvora.model.Category;
import com.example.elvora.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category) {

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategory() {

        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {

        return categoryRepository.findById(id).orElse(null);
    }

    public Category updateCategory(Category category) {

        return categoryRepository.save(category);
    }

    public void deleteCategory(int id) {

        categoryRepository.deleteById(id);
    }

    public List<Category> getCategoryByCreatedBy(String email) {

        return categoryRepository.findByCreatedBy(email);
    }

    public List<Category> getUserCategories(
            String userEmail,
            List<String> adminEmails) {

        List<Category> categories = new ArrayList<>();

        for (String adminEmail : adminEmails) {

            categories.addAll(
                    categoryRepository.findByCreatedBy(adminEmail)
            );
        }

        categories.addAll(
                categoryRepository.findByCreatedBy(userEmail)
        );

        return categories;
    }

}