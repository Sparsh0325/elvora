package com.example.elvora.service;

import com.example.elvora.repository.BudgetRepository;
import com.example.elvora.repository.TransactionRepository;
import com.example.elvora.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class AdminReportService {

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final BudgetRepository budgetRepository;


    public AdminReportService(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            BudgetRepository budgetRepository) {

        this.userRepository = userRepository;

        this.transactionRepository = transactionRepository;

        this.budgetRepository = budgetRepository;
    }


    public long getTotalUsers() {

        return userRepository.count();
    }


    public long getTotalTransactions() {

        return transactionRepository.count();
    }


    public double getTotalBudget() {

        return budgetRepository.getTotalBudget();
    }


    public double getTotalExpense() {

        return transactionRepository.getTotalExpense();
    }
}