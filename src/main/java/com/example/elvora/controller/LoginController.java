package com.example.elvora.controller;

import com.example.elvora.dto.LoginDto;
import com.example.elvora.dto.UserDto;
import com.example.elvora.model.Admin;
import com.example.elvora.model.User;
import com.example.elvora.service.AdminService;
import com.example.elvora.service.LoginService;
import com.example.elvora.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;
    private final AdminService adminService;

    public LoginController(LoginService loginService,
                           UserService userService,
                           AdminService adminService) {
        this.loginService = loginService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {

        LoginDto loginDto = new LoginDto();

        model.addAttribute("loginDto", loginDto);

        return "loginform";
    }

    @PostMapping("/login")
    public String login(
            HttpServletRequest request,
            Model model,
            @Validated @ModelAttribute LoginDto loginDto,
            BindingResult result) {

        String ut = loginService.checkLogin(loginDto);

        if (ut != null) {

            HttpSession session =
                    request.getSession(true);

            session.setAttribute(
                    "email",
                    loginDto.getEmail()
            );

            session.setAttribute(
                    "usertype",
                    ut
            );

            // Normalised role for the interceptor
            String role = ut.equalsIgnoreCase("admin")
                    ? "ADMIN"
                    : "USER";

            session.setAttribute("role", role);

            if ("ADMIN".equals(role)) {
                Admin admin = adminService.getAdminByEmail(loginDto.getEmail());
                session.setAttribute("displayName", admin != null ? admin.getAdminname() : "Admin");
                return "redirect:/admin/home";

            } else {
                User user = userService.getUserByEmail(loginDto.getEmail());
                session.setAttribute("displayName", user != null ? user.getName() : "User");
                return "redirect:/user/home";
            }
        }

        return "loginform";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session != null) {

            session.invalidate();
        }

        return "redirect:/";
    }

    @GetMapping("/AuthError")
    public String authError() {

        return "AuthError";
    }

    // ============================================
    //  PUBLIC REGISTRATION (for guest sign-ups)
    // ============================================

    @GetMapping("/register")
    public String showRegisterPage(Model model) {

        model.addAttribute("userDto", new UserDto());

        return "PublicRegistration";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute UserDto userDto) {

        User user = new User();

        user.setName(userDto.getName());
        user.setContact(userDto.getContact());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());

        userService.registerUser(
                user,
                userDto.getPassword()
        );

        return "redirect:/login";
    }
}