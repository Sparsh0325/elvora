package com.example.elvora.dto;

public class MonthlyReportDto {

    private int month;

    private double budget;

    private double expense;

    private double remaining;


    public MonthlyReportDto() {
    }


    public MonthlyReportDto(
            int month,
            double budget,
            double expense,
            double remaining) {

        this.month = month;

        this.budget = budget;

        this.expense = expense;

        this.remaining = remaining;
    }


    public int getMonth() {

        return month;
    }


    public void setMonth(int month) {

        this.month = month;
    }


    public double getBudget() {

        return budget;
    }


    public void setBudget(double budget) {

        this.budget = budget;
    }


    public double getExpense() {

        return expense;
    }


    public void setExpense(double expense) {

        this.expense = expense;
    }


    public double getRemaining() {

        return remaining;
    }


    public void setRemaining(double remaining) {

        this.remaining = remaining;
    }
}