package com.example.backtestapp.model;

import lombok.Data;
import java.util.List;

@Data
public class BacktestResult {
    private double totalInvestment;
    private double finalValue;
    private double totalReturn;
    private double annualizedReturn;
    private double sharpeRatio;
    private List<MonthlyData> monthlyData;

    @Data
    public static class MonthlyData {
        private String date;
        private double investment;
        private double portfolioValue;
        private double returnRate;
    }
} 