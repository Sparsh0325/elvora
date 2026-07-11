package com.example.elvora.service;

import com.example.elvora.model.Budget;
import com.example.elvora.repository.BudgetRepository;
import com.example.elvora.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    private final TransactionRepository transactionRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            TransactionRepository transactionRepository) {

        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
    }

    public Budget addBudget(Budget budget) {

        return budgetRepository.save(budget);
    }

    public Budget getBudgetById(int id) {

        return budgetRepository
                .findById(id)
                .orElse(null);
    }

    public Budget getMonthlyBudget(
            String email,
            int month,
            int year) {

        return budgetRepository
                .findByUserEmailAndMonthAndYear(
                        email,
                        month,
                        year
                );
    }

    public List<Budget> getYearlyBudgets(
            String email,
            int year) {

        return budgetRepository
                .findByUserEmailAndYear(
                        email,
                        year
                );
    }

    public Budget updateBudget(Budget budget) {

        return budgetRepository.save(budget);
    }

    public double getMonthlyExpense(
            String email,
            int month,
            int year) {

        return transactionRepository
                .getMonthlyExpense(
                        email,
                        month,
                        year
                );
    }

    public double getYearlyExpense(
            String email,
            int year) {

        return transactionRepository
                .getYearlyExpense(
                        email,
                        year
                );
    }

    public double calculateYearlyBudget(
            String email,
            int year) {

        List<Budget> budgets =
                getYearlyBudgets(email, year);

        double total = 0;

        for (Budget budget : budgets) {

            total = total + budget.getAmount();
        }

        return total;
    }
}