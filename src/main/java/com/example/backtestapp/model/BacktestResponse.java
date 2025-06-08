package com.example.backtestapp.model;

import java.util.List;
import com.example.backtestapp.model.MonthlyData;

public class BacktestResponse {
    private List<SymbolResult> results;
    private CombinedResult combinedResult;

    public List<SymbolResult> getResults() {
        return results;
    }

    public void setResults(List<SymbolResult> results) {
        this.results = results;
    }

    public CombinedResult getCombinedResult() {
        return combinedResult;
    }

    public void setCombinedResult(CombinedResult combinedResult) {
        this.combinedResult = combinedResult;
    }

    public static class CombinedResult {
        private double totalInvestment;
        private double finalValue;
        private double totalReturn;
        private double annualizedReturn;
        private double sharpeRatio;
        private List<MonthlyData> monthlyData;

        public double getTotalInvestment() { return totalInvestment; }
        public void setTotalInvestment(double totalInvestment) { this.totalInvestment = totalInvestment; }
        public double getFinalValue() { return finalValue; }
        public void setFinalValue(double finalValue) { this.finalValue = finalValue; }
        public double getTotalReturn() { return totalReturn; }
        public void setTotalReturn(double totalReturn) { this.totalReturn = totalReturn; }
        public double getAnnualizedReturn() { return annualizedReturn; }
        public void setAnnualizedReturn(double annualizedReturn) { this.annualizedReturn = annualizedReturn; }
        public double getSharpeRatio() { return sharpeRatio; }
        public void setSharpeRatio(double sharpeRatio) { this.sharpeRatio = sharpeRatio; }
        public List<MonthlyData> getMonthlyData() { return monthlyData; }
        public void setMonthlyData(List<MonthlyData> monthlyData) { this.monthlyData = monthlyData; }
    }
} 