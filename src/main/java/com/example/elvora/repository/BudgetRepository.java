package com.example.elvora.repository;

import com.example.elvora.model.Budget;
import com.example.elvora.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetRepository
        extends JpaRepository<Budget, Integer> {

    Budget findByUserEmailAndMonthAndYear(
            String email,
            int month,
            int year
    );

    List<Budget> findByUserEmailAndYear(
            String email,
            int year
    );

    @Query("""
            SELECT COALESCE(SUM(b.amount), 0)
            FROM Budget b
            """)
    Double getTotalBudget();

    void deleteByUser(User user);
}