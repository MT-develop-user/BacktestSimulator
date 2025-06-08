package com.example.backtestapp.model;

import java.util.List;

public class BacktestRequest {
    private List<SymbolRequest> symbols;
    private String startDate;
    private String endDate;

    public List<SymbolRequest> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<SymbolRequest> symbols) {
        this.symbols = symbols;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
} 