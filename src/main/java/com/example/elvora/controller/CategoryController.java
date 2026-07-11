package com.example.elvora.controller;

import com.example.elvora.dto.CategoryDto;
import com.example.elvora.model.Category;
import com.example.elvora.model.Login;
import com.example.elvora.repository.LoginRepository;
import com.example.elvora.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final LoginRepository loginRepository;

    public CategoryController(
            CategoryService categoryService,
            LoginRepository loginRepository) {

        this.categoryService = categoryService;
        this.loginRepository = loginRepository;
    }

    @GetMapping("/register")
    public String registrationPage(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        model.addAttribute("categoryDto", new CategoryDto());
        
        if ("ADMIN".equals(role)) {
            return "Admin/AddCategory";
        } else {
            return "User/AddCategory";
        }
    }

    @PostMapping("/register")
    public String registerCategory(
            HttpSession session,
            @ModelAttribute CategoryDto dto) {

        String email = (String) session.getAttribute("email");

        Category category = new Category();
        category.setCategory_name(dto.getCategory_name());
        category.setCreated_by(email);

        categoryService.addCategory(category);

        return "redirect:/category/showall";
    }

    @GetMapping("/showall")
    public String showAll(
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("email");
        String role = (String) session.getAttribute("role");

        List<Category> categories;

        if ("ADMIN".equals(role)) {
            // Admins see categories created by themselves
            categories = categoryService.getCategoryByCreatedBy(email);
        } else {
            // Users see: admin-created categories + their own
            List<Category> allCategories = categoryService.getAllCategory();
            categories = new ArrayList<>();

            for (Category category : allCategories) {
                Login creator = loginRepository.findById(category.getCreated_by()).orElse(null);
                if (creator != null && creator.getUsertype().equalsIgnoreCase("admin")) {
                    categories.add(category);
                } else if (category.getCreated_by().equals(email)) {
                    categories.add(category);
                }
            }
        }

        model.addAttribute("categories", categories);
        model.addAttribute("email", email);

        if ("ADMIN".equals(role)) {
            return "Admin/ShowCategory";
        } else {
            return "User/ShowCategory";
        }
    }

    @GetMapping("/edit")
    public String editCategory(
            @RequestParam("id") int id,
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("email");

        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            return "redirect:/category/showall";
        }

        if (!category.getCreated_by().equals(email)) {
            return "redirect:/category/showall";
        }

        model.addAttribute("category", category);

        return "Category/EditCategory";
    }

    @PostMapping("/edit")
    public String updateCategory(
            HttpSession session,
            @ModelAttribute CategoryDto dto) {

        String email = (String) session.getAttribute("email");

        Category category = categoryService.getCategoryById(dto.getCategory_id());

        if (category == null) {
            return "redirect:/category/showall";
        }

        if (!category.getCreated_by().equals(email)) {
            return "redirect:/category/showall";
        }

        category.setCategory_name(dto.getCategory_name());
        categoryService.updateCategory(category);

        return "redirect:/category/showall";
    }

    @GetMapping("/delete")
    public String deletePage(
            @RequestParam("id") int id,
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("email");

        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            return "redirect:/category/showall";
        }

        if (!category.getCreated_by().equals(email)) {
            return "redirect:/category/showall";
        }

        model.addAttribute("category", category);

        return "Category/DeleteCategory";
    }

    @PostMapping("/delete")
    public String deleteCategory(
            @RequestParam("category_id") int id,
            HttpSession session) {

        String email = (String) session.getAttribute("email");

        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            return "redirect:/category/showall";
        }

        if (!category.getCreated_by().equals(email)) {
            return "redirect:/category/showall";
        }

        categoryService.deleteCategory(id);

        return "redirect:/category/showall";
    }
}