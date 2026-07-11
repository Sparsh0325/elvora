package com.example.elvora.controller;

import com.example.elvora.dto.BudgetDto;
import com.example.elvora.model.Budget;
import com.example.elvora.model.User;
import com.example.elvora.repository.UserRepository;
import com.example.elvora.service.BudgetService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    private final UserRepository userRepository;

    public BudgetController(
            BudgetService budgetService,
            UserRepository userRepository) {

        this.budgetService = budgetService;
        this.userRepository = userRepository;
    }


    @GetMapping("/add")
    public String addBudgetPage(
            Model model) {

        model.addAttribute(
                "budgetDto",
                new BudgetDto()
        );

        return "Budget/AddBudget";
    }


    @PostMapping("/add")
    public String addBudget(
            HttpSession session,
            @ModelAttribute BudgetDto budgetDto) {

        String email =
                (String) session.getAttribute("email");

        User user =
                userRepository.findByEmail(email);

        if (user == null) {

            return "redirect:/AuthError";
        }

        Budget existingBudget =
                budgetService.getMonthlyBudget(
                        email,
                        budgetDto.getMonth(),
                        budgetDto.getYear()
                );

        if (existingBudget != null) {

            return "redirect:/budget/add?exists";
        }

        Budget budget = new Budget();

        budget.setUser(user);

        budget.setAmount(
                budgetDto.getAmount()
        );

        budget.setMonth(
                budgetDto.getMonth()
        );

        budget.setYear(
                budgetDto.getYear()
        );

        budgetService.addBudget(budget);

        return "redirect:/budget/show";
    }


    @GetMapping("/show")
    public String showBudget(
            HttpSession session,
            @RequestParam(
                    value = "month",
                    required = false
            ) Integer month,
            @RequestParam(
                    value = "year",
                    required = false
            ) Integer year,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        LocalDate today =
                LocalDate.now();

        if (month == null) {

            month = today.getMonthValue();
        }

        if (year == null) {

            year = today.getYear();
        }

        Budget budget =
                budgetService.getMonthlyBudget(
                        email,
                        month,
                        year
                );

        double monthlyExpense =
                budgetService.getMonthlyExpense(
                        email,
                        month,
                        year
                );

        double monthlyBudget = 0;

        int budgetId = 0;

        if (budget != null) {

            monthlyBudget =
                    budget.getAmount();

            budgetId =
                    budget.getBudget_id();
        }

        double monthlyRemaining =
                monthlyBudget - monthlyExpense;

        double yearlyBudget =
                budgetService.calculateYearlyBudget(
                        email,
                        year
                );

        double yearlyExpense =
                budgetService.getYearlyExpense(
                        email,
                        year
                );

        double yearlyRemaining =
                yearlyBudget - yearlyExpense;

        model.addAttribute("budget", budget);
        model.addAttribute("budgetId", budgetId);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("monthlyBudget", monthlyBudget);
        model.addAttribute("monthlyExpense", monthlyExpense);
        model.addAttribute("monthlyRemaining", monthlyRemaining);
        model.addAttribute("yearlyBudget", yearlyBudget);
        model.addAttribute("yearlyExpense", yearlyExpense);
        model.addAttribute("yearlyRemaining", yearlyRemaining);

        return "Budget/ShowBudget";
    }


    @GetMapping("/edit")
    public String editBudgetPage(
            @RequestParam("id") int id,
            HttpSession session,
            Model model) {

        String email =
                (String) session.getAttribute("email");

        Budget budget =
                budgetService.getBudgetById(id);

        if (budget == null) {

            return "redirect:/budget/show";
        }

        if (!budget
                .getUser()
                .getEmail()
                .equals(email)) {

            return "redirect:/AuthError";
        }

        model.addAttribute("budget", budget);

        return "Budget/EditBudget";
    }


    @PostMapping("/edit")
    public String updateBudget(
            HttpSession session,
            @ModelAttribute BudgetDto budgetDto) {

        String email =
                (String) session.getAttribute("email");

        Budget budget =
                budgetService.getBudgetById(
                        budgetDto.getBudget_id()
                );

        if (budget == null) {

            return "redirect:/budget/show";
        }

        if (!budget
                .getUser()
                .getEmail()
                .equals(email)) {

            return "redirect:/AuthError";
        }

        budget.setAmount(
                budgetDto.getAmount()
        );

        budgetService.updateBudget(budget);

        return "redirect:/budget/show?month="
                + budget.getMonth()
                + "&year="
                + budget.getYear();
    }
}