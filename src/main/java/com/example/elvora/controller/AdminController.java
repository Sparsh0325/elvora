package com.example.elvora.controller;

import com.example.elvora.dto.AdminDto;
import com.example.elvora.dto.CategoryDto;
import com.example.elvora.dto.UserDto;
import com.example.elvora.model.Admin;
import com.example.elvora.model.Category;
import com.example.elvora.model.User;
import com.example.elvora.service.AdminReportService;
import com.example.elvora.service.AdminService;
import com.example.elvora.service.CategoryService;
import com.example.elvora.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final AdminReportService adminReportService;
    private final UserService userService;
    private final CategoryService categoryService;

    public AdminController(
            AdminService adminService,
            AdminReportService adminReportService,
            UserService userService,
            CategoryService categoryService) {

        this.adminService = adminService;
        this.adminReportService = adminReportService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // ============================================================
    //  ADMIN HOME
    // ============================================================

    @GetMapping("/home")
    public String adminHome(HttpSession session, Model model) {

        String email =
                (String) session.getAttribute("email");

        Admin admin =
                adminService.getAdminByEmail(email);

        model.addAttribute("admin", admin);

        return "Admin/AdminHome";
    }

    // ============================================================
    //  ADMIN REGISTRATION
    // ============================================================

    @GetMapping("/register")
    public String registrationPage() {

        return "Admin/AdminRegistration";
    }

    @PostMapping("/register")
    public String registerAdmin(
            @ModelAttribute AdminDto adminDto) {

        Admin admin = new Admin();

        admin.setAdminname(adminDto.getAdminname());
        admin.setAddress(adminDto.getAddress());
        admin.setContact(adminDto.getContact());
        admin.setEmail(adminDto.getEmail());

        adminService.registerAdmin(
                admin,
                adminDto.getPassword()
        );

        return "redirect:/admin/profile?email="
                + admin.getEmail();
    }

    // ============================================================
    //  ADMIN PROFILE / EDIT
    // ============================================================

    @GetMapping("/showall")
    public String showAllAdmin(Model model) {

        model.addAttribute(
                "admins",
                adminService.getAllAdmin()
        );

        return "Admin/ShowAdmin";
    }

    @GetMapping("/profile")
    public String adminProfile(
            @RequestParam("email") String email,
            Model model) {

        Admin admin = adminService.getAdminByEmail(email);

        model.addAttribute("admin", admin);

        return "Admin/AdminProfile";
    }

    @GetMapping("/edit")
    public String editAdmin(
            @RequestParam("email") String email,
            Model model) {

        Admin admin = adminService.getAdminByEmail(email);

        model.addAttribute("admin", admin);

        return "Admin/EditAdmin";
    }

    @PostMapping("/edit")
    public String updateAdmin(
            @RequestParam("adminname") String adminname,
            @RequestParam("address") String address,
            @RequestParam("contact") Long contact,
            @RequestParam("email") String email) {

        Admin admin = new Admin();

        admin.setAdminname(adminname);
        admin.setAddress(address);
        admin.setContact(contact);
        admin.setEmail(email);

        adminService.updateAdmin(admin);

        return "redirect:/admin/profile?email=" + admin.getEmail();
    }

    // ============================================================
    //  ADMIN CHANGE PASSWORD
    // ============================================================

    @GetMapping("/changePassword")
    public String changePasswordPage(
            HttpSession session,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        Admin admin =
                adminService.getAdminByEmail(email);

        model.addAttribute("admin", admin);

        return "ChangePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(
            HttpSession session,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        Admin admin =
                adminService.getAdminByEmail(email);

        boolean result =
                adminService.changePassword(
                        email,
                        oldPassword,
                        newPassword
                );

        if (result) {
            return "redirect:/admin/home";
        }

        model.addAttribute(
                "msg",
                "Old Password Incorrect"
        );

        model.addAttribute(
                "admin",
                admin
        );

        return "ChangePassword";
    }

    // ============================================================
    //  ADMIN REPORTS
    // ============================================================

    @GetMapping("/reports")
    public String adminReports(
            HttpSession session,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        Admin admin =
                adminService.getAdminByEmail(email);

        long totalUsers =
                adminReportService.getTotalUsers();

        long totalTransactions =
                adminReportService.getTotalTransactions();

        double totalBudget =
                adminReportService.getTotalBudget();

        double totalExpense =
                adminReportService.getTotalExpense();

        model.addAttribute("admin", admin);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("totalBudget", totalBudget);
        model.addAttribute("totalExpense", totalExpense);

        return "Admin/AdminReport";
    }

    // ============================================================
    //  USER MANAGEMENT  (/admin/user/**)
    // ============================================================

    @GetMapping("/user/showall")
    public String showAllUsers(Model model) {

        model.addAttribute("users", userService.getAllUser());

        return "User/ShowUser";
    }

    @GetMapping("/user/register")
    public String showAddUserPage() {

        return "User/UserRegistration";
    }

    @PostMapping("/user/register")
    public String addUser(@ModelAttribute UserDto userDto) {

        User user = new User();

        user.setName(userDto.getName());
        user.setContact(userDto.getContact());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());

        User savedUser = userService.registerUser(
                user,
                userDto.getPassword()
        );

        return "redirect:/admin/user/showall";
    }

    @GetMapping("/user/edit")
    public String editUserPage(
            @RequestParam("user_id") int userId,
            Model model) {

        User user = userService.getUserById(userId);

        model.addAttribute("user", user);

        return "User/EditUser";
    }

    @PostMapping("/user/edit")
    public String updateUser(@ModelAttribute UserDto userDto) {

        User user = new User();

        user.setUser_id(userDto.getUser_id());
        user.setName(userDto.getName());
        user.setContact(userDto.getContact());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());

        userService.updateUser(user);

        return "redirect:/admin/user/showall";
    }

    @GetMapping("/user/delete")
    public String showDeleteUserPage(
            @RequestParam("user_id") int userId,
            Model model) {

        User user = userService.getUserById(userId);

        model.addAttribute("user", user);

        return "User/DeleteUser";
    }

    @PostMapping("/user/delete")
    public String deleteUser(
            @RequestParam("user_id") int userId) {

        userService.deleteUser(userId);

        return "redirect:/admin/user/showall";
    }

}