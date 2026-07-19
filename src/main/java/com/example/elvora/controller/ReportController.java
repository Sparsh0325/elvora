package com.example.elvora.controller;

import com.example.elvora.model.Budget;
import com.example.elvora.model.Transaction;
import com.example.elvora.service.BudgetService;
import com.example.elvora.service.TransactionService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final BudgetService budgetService;
    private final TransactionService transactionService;

    public ReportController(
            BudgetService budgetService,
            TransactionService transactionService) {

        this.budgetService = budgetService;
        this.transactionService = transactionService;
    }

    // ===============================
    // MONTHLY REPORT
    // ===============================

    @GetMapping("/monthly")
    public String monthlyReport(

            HttpSession session,

            @RequestParam(value = "month", required = false)
            Integer month,

            @RequestParam(value = "year", required = false)
            Integer year,

            Model model) {

        String email =
                (String) session.getAttribute("email");

        LocalDate today = LocalDate.now();

        if (month == null) {

            month = today.getMonthValue();
        }

        if (year == null) {

            year = today.getYear();
        }

        String monthName =
                Month.of(month)
                        .name()
                        .substring(0,1)
                        + Month.of(month)
                        .name()
                        .substring(1)
                        .toLowerCase();

        model.addAttribute("monthName", monthName);

        Budget budget =
                budgetService.getMonthlyBudget(
                        email,
                        month,
                        year
                );

        double budgetAmount = 0;

        if (budget != null) {

            budgetAmount = budget.getAmount();
        }

        double expense =
                budgetService.getMonthlyExpense(
                        email,
                        month,
                        year
                );

        double remaining =
                budgetAmount - expense;

        List<Transaction> transactions =
                transactionService
                        .getMonthlyExpenseTransactions(
                                email,
                                month,
                                year
                        );

        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("monthlyBudget", budgetAmount);
        model.addAttribute("monthlyExpense", expense);
        model.addAttribute("monthlyRemaining", remaining);
        model.addAttribute("transactions", transactions);

        return "User/MonthlyReport";
    }


    // ===============================
    // YEARLY REPORT
    // ===============================

    @GetMapping("/yearly")
    public String yearlyReport(

            HttpSession session,

            @RequestParam(value = "year", required = false)
            Integer year,

            Model model) {

        String email =
                (String) session.getAttribute("email");

        if (year == null) {

            year = LocalDate.now().getYear();
        }

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

        List<Transaction> transactions =
                transactionService
                        .getYearlyExpenseTransactions(
                                email,
                                year
                        );

        model.addAttribute("year", year);
        model.addAttribute("yearlyBudget", yearlyBudget);
        model.addAttribute("yearlyExpense", yearlyExpense);
        model.addAttribute("yearlyRemaining", yearlyRemaining);
        model.addAttribute("transactions", transactions);

        return "User/YearlyReport";
    }

}