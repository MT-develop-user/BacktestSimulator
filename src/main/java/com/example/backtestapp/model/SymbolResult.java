package com.example.backtestapp.model;

import java.util.List;

public class SymbolResult {
    private String symbol;
    private double totalInvestment;
    private double finalValue;
    private double totalReturn;
    private double annualizedReturn;
    private double sharpeRatio;
    private List<MonthlyData> monthlyData;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public double getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(double finalValue) {
        this.finalValue = finalValue;
    }

    public double getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(double totalReturn) {
        this.totalReturn = totalReturn;
    }

    public double getAnnualizedReturn() {
        return annualizedReturn;
    }

    public void setAnnualizedReturn(double annualizedReturn) {
        this.annualizedReturn = annualizedReturn;
    }

    public double getSharpeRatio() {
        return sharpeRatio;
    }

    public void setSharpeRatio(double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    public List<MonthlyData> getMonthlyData() {
        return monthlyData;
    }

    public void setMonthlyData(List<MonthlyData> monthlyData) {
        this.monthlyData = monthlyData;
    }
} 