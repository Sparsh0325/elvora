package com.example.elvora.repository;

import com.example.elvora.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUserEmailAndType(
            String email,
            String type
    );


    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user.email = :email
            AND t.type = 'EXPENSE'
            AND MONTH(t.date) = :month
            AND YEAR(t.date) = :year
            """)
    Double getMonthlyExpense(
            @Param("email") String email,
            @Param("month") int month,
            @Param("year") int year
    );


    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user.email = :email
            AND t.type = 'EXPENSE'
            AND YEAR(t.date) = :year
            """)
    Double getYearlyExpense(
            @Param("email") String email,
            @Param("year") int year
    );


    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.type = 'EXPENSE'
            """)
    Double getTotalExpense();


    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.user.email = :email
            AND t.type = 'EXPENSE'
            AND MONTH(t.date) = :month
            AND YEAR(t.date) = :year
            ORDER BY t.date ASC
            """)
    List<Transaction> getMonthlyExpenseTransactions(
            @Param("email") String email,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
       SELECT t
       FROM Transaction t
       WHERE t.user.email = :email
       AND t.type = 'EXPENSE'
       AND YEAR(t.date)=:year
       ORDER BY t.date ASC
       """)
    List<Transaction> getYearlyExpenseTransactions(
            @Param("email") String email,
            @Param("year") int year
    );
}