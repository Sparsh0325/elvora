package com.example.elvora.controller;

import com.example.elvora.dto.UserDto;
import com.example.elvora.model.User;
import com.example.elvora.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final com.example.elvora.service.TransactionService transactionService;
    private final com.example.elvora.service.BudgetService budgetService;

    public UserController(UserService userService,
                          com.example.elvora.service.TransactionService transactionService,
                          com.example.elvora.service.BudgetService budgetService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.budgetService = budgetService;
    }

    // ============================================================
    //  USER HOME
    // ============================================================

    @GetMapping("/home")
    public String home(HttpSession session,
                       Model model) {

        String email =
                (String) session.getAttribute("email");

        User user = userService.getUserByEmail(email);

        java.time.LocalDate today = java.time.LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        com.example.elvora.model.Budget budget = budgetService.getMonthlyBudget(email, month, year);
        double monthlyBudget = budget != null ? budget.getAmount() : 0.0;
        double monthlyExpense = budgetService.getMonthlyExpense(email, month, year);
        double monthlyRemaining = monthlyBudget - monthlyExpense;

        List<com.example.elvora.model.Transaction> expenses = transactionService.getUserExpenses(email);
        List<com.example.elvora.model.Transaction> recentExpenses = expenses.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .limit(7)
                .collect(java.util.stream.Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("monthlyBudget", monthlyBudget);
        model.addAttribute("monthlyExpense", monthlyExpense);
        model.addAttribute("monthlyRemaining", monthlyRemaining);
        model.addAttribute("recentExpenses", recentExpenses);

        return "User/UserHome";
    }

    // ============================================================
    //  USER PROFILE
    // ============================================================

    @GetMapping("/profile")
    public String profile(HttpSession session,
                          Model model) {

        String email =
                (String) session.getAttribute("email");

        User user = userService.getUserByEmail(email);

        model.addAttribute("user", user);

        return "User/UserProfile";
    }

    // ============================================================
    //  USER SELF-EDIT
    // ============================================================

    @GetMapping("/edit")
    public String editUser(HttpSession session,
                           Model model) {

        String email =
                (String) session.getAttribute("email");

        User user = userService.getUserByEmail(email);

        model.addAttribute("user", user);

        return "User/EditUser";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute UserDto userDto) {

        User user = new User();

        user.setUser_id(userDto.getUser_id());
        user.setName(userDto.getName());
        user.setContact(userDto.getContact());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());

        userService.updateUser(user);

        return "redirect:/user/profile";
    }

    // ============================================================
    //  USER SELF-DELETE
    // ============================================================

    @PostMapping("/delete")
    public String deleteUser(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        String email =
                (String) session.getAttribute("email");

        User user = userService.getUserByEmail(email);

        if (user != null) {
            userService.deleteUser(user.getUser_id());
        }

        session.invalidate();

        return "redirect:/login";
    }

    // ============================================================
    //  USER CHANGE PASSWORD
    // ============================================================

    @GetMapping("/changePassword")
    public String changePasswordPage() {

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

        boolean result =
                userService.changePassword(
                        email,
                        oldPassword,
                        newPassword
                );

        if (result) {
            return "redirect:/user/home";
        }

        model.addAttribute(
                "msg",
                "Old Password Incorrect"
        );

        return "ChangePassword";
    }
}
