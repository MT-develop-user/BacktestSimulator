package com.example.backtestapp.controller;

import com.example.backtestapp.model.BacktestRequest;
import com.example.backtestapp.model.BacktestResponse;
import com.example.backtestapp.service.BacktestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BacktestController {
    
    @Autowired
    private BacktestService backtestService;
    
    @PostMapping("/backtest")
    public BacktestResponse performBacktest(@RequestBody BacktestRequest request) {
        return backtestService.performBacktest(request);
    }
} 