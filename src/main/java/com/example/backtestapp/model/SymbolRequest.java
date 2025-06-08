package com.example.backtestapp.model;

public class SymbolRequest {
    private String symbol;
    private double initialInvestment;
    private double monthlyInvestment;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(double initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    public double getMonthlyInvestment() {
        return monthlyInvestment;
    }

    public void setMonthlyInvestment(double monthlyInvestment) {
        this.monthlyInvestment = monthlyInvestment;
    }
} 