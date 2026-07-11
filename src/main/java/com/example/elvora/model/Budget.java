package com.example.elvora.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "budgetdata",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "user_id",
                                "month",
                                "year"
                        }
                )
        }
)
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int budget_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private float amount;

    private int month;

    private int year;

    public Budget() {
    }

    public Budget(int budget_id,
                  User user,
                  float amount,
                  int month,
                  int year) {

        this.budget_id = budget_id;
        this.user = user;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    public int getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(int budget_id) {
        this.budget_id = budget_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "budget_id=" + budget_id +
                ", user=" + user +
                ", amount=" + amount +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}