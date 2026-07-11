package com.example.elvora.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactiondata")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transaction_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private float amount;

    private String type;

    private LocalDate date;

    private String description;

    public Transaction() {
    }

    public Transaction(User user, Category category, float amount,
                       String type, LocalDate date, String description) {
        this.user = user;
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.description = description;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transaction_id=" + transaction_id +
                ", user=" + user +
                ", category=" + category +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}