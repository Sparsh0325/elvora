package com.example.elvora.service;

import com.example.elvora.model.Transaction;
import com.example.elvora.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(
            TransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    public Transaction addExpense(
            Transaction transaction) {

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserExpenses(
            String email) {

        return transactionRepository
                .findByUserEmailAndType(
                        email,
                        "EXPENSE"
                );
    }

    public Transaction getTransactionById(
            int id) {

        return transactionRepository
                .findById(id)
                .orElse(null);
    }

    public Transaction updateExpense(
            Transaction transaction) {

        return transactionRepository.save(transaction);
    }

    public void deleteExpense(
            int id) {

        transactionRepository.deleteById(id);
    }

    public List<Transaction> getMonthlyExpenseTransactions(
            String email,
            int month,
            int year) {

        return transactionRepository.getMonthlyExpenseTransactions(
                email,
                month,
                year
        );
    }

    public List<Transaction> getYearlyExpenseTransactions(
            String email,
            int year) {

        return transactionRepository.getYearlyExpenseTransactions(
                email,
                year
        );
    }
}