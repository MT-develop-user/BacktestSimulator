package com.example.backtestapp.model;

public class MonthlyData {
    private String date;
    private double investment;
    private double portfolioValue;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getInvestment() {
        return investment;
    }

    public void setInvestment(double investment) {
        this.investment = investment;
    }

    public double getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(double portfolioValue) {
        this.portfolioValue = portfolioValue;
    }
} 