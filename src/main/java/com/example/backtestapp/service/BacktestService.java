package com.example.backtestapp.service;

import com.example.backtestapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BacktestService {
    private static final Logger logger = LoggerFactory.getLogger(BacktestService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public BacktestResponse performBacktest(BacktestRequest request) {
        List<SymbolResult> results = new ArrayList<>();
        
        for (SymbolRequest symbolRequest : request.getSymbols()) {
            SymbolResult result = performSingleBacktest(symbolRequest, request.getStartDate(), request.getEndDate());
            results.add(result);
        }
        
        BacktestResponse response = new BacktestResponse();
        response.setResults(results);
        
        // 合算値の計算 (2銘柄以上の場合)
        if (results.size() > 1) {
            BacktestResponse.CombinedResult combined = new BacktestResponse.CombinedResult();
            
            double totalInvestment = 0;
            double finalValue = 0;
            for (SymbolResult result : results) {
                totalInvestment += result.getTotalInvestment();
                finalValue += result.getFinalValue();
            }

            // トータルリターン（合算）
            double totalReturn = ((finalValue - totalInvestment) / totalInvestment) * 100;
            
            // 年率リターン（合算）
            String startDate = request.getStartDate();
            String endDate = request.getEndDate();
            java.time.LocalDate startLocalDate = java.time.LocalDate.parse(startDate);
            java.time.LocalDate endLocalDate = java.time.LocalDate.parse(endDate);
            double years = java.time.temporal.ChronoUnit.DAYS.between(startLocalDate, endLocalDate) / 365.0;
            double annualizedReturn = (Math.pow(1 + totalReturn / 100, 1 / years) - 1) * 100;

            // 月次データ（日付で合算）
            java.util.List<MonthlyData> monthlyDataList = new java.util.ArrayList<>();
            if (!results.isEmpty()) {
                // 全ての月次データリストの中で最も短いものに長さを合わせる
                int minLength = results.stream()
                                       .mapToInt(r -> r.getMonthlyData().size())
                                       .min()
                                       .orElse(0);

                for (int i = 0; i < minLength; i++) {
                    double monthlyInvestmentSum = 0;
                    double monthlyPortfolioValueSum = 0;
                    String date = results.get(0).getMonthlyData().get(i).getDate();

                    for (SymbolResult result : results) {
                        monthlyInvestmentSum += result.getMonthlyData().get(i).getInvestment();
                        monthlyPortfolioValueSum += result.getMonthlyData().get(i).getPortfolioValue();
                    }
                    
                    MonthlyData md = new MonthlyData();
                    md.setDate(date);
                    md.setInvestment(monthlyInvestmentSum);
                    md.setPortfolioValue(monthlyPortfolioValueSum);
                    monthlyDataList.add(md);
                }
            }
            
            // シャープレシオ（合算）
            double sharpeRatio = calculateSharpeRatio(monthlyDataList, annualizedReturn);
            
            combined.setTotalInvestment(totalInvestment);
            combined.setFinalValue(finalValue);
            combined.setTotalReturn(totalReturn);
            combined.setAnnualizedReturn(annualizedReturn);
            combined.setSharpeRatio(sharpeRatio);
            combined.setMonthlyData(monthlyDataList);
            response.setCombinedResult(combined);
        }
        
        return response;
    }

    private SymbolResult performSingleBacktest(SymbolRequest request, String startDate, String endDate) {
        String url = String.format(
            "https://stooq.com/q/d/l/?s=%s&d1=%s&d2=%s&i=d",
            request.getSymbol(),
            startDate.replace("-", ""),
            endDate.replace("-", "")
        );
        
        logger.info("Requesting data from URL: " + url);
        
        String csvData = restTemplate.getForObject(url, String.class);
        if (csvData == null || csvData.trim().isEmpty() || csvData.trim().equals("No data")) {
            logger.error("No data found in response for symbol " + request.getSymbol() + ". Full response content: " + csvData);
            throw new RuntimeException("銘柄「" + request.getSymbol() + "」のデータが、選択された期間に見つかりませんでした。銘柄コードが古い、上場廃止、または指定期間が適切でない可能性があります。");
        }
        
        logger.info("First line of response: " + csvData.lines().findFirst().orElse(""));
        
        List<String> lines = csvData.lines()
            .skip(1)  // ヘッダーをスキップ
            .filter(line -> !line.trim().isEmpty())
            .collect(Collectors.toList());
            
        if (lines.isEmpty()) {
            logger.error("No data found in response for symbol " + request.getSymbol() + ". Full response content: " + csvData);
            throw new RuntimeException("銘柄「" + request.getSymbol() + "」のデータが、選択された期間に見つかりませんでした。銘柄コードが古い、上場廃止、または指定期間が適切でない可能性があります。");
        }

        // 日付でソート（古い順）
        lines.sort(Comparator.comparing(line -> line.split(",")[0]));

        // 結果オブジェクトの初期化
        SymbolResult result = new SymbolResult();
        result.setSymbol(request.getSymbol());
        List<MonthlyData> monthlyDataList = new ArrayList<>();
        
        // 初期値の設定
        double totalInvestment = request.getInitialInvestment();
        double currentShares = 0;
        String previousMonth = "";
        
        // 最初の日のデータを取得
        String[] firstDayData = lines.get(0).split(",");
        double firstDayPrice = Double.parseDouble(firstDayData[4]); // Close price
        currentShares = totalInvestment / firstDayPrice;
        
        for (String line : lines) {
            String[] data = line.split(",");
            String date = data[0];
            double closePrice = Double.parseDouble(data[4]);
            
            String currentMonth = date.substring(0, 7);
            // 月が変わった時の処理（ただし、最初の月は除く）
            if (!currentMonth.equals(previousMonth) && !previousMonth.isEmpty()) {
                totalInvestment += request.getMonthlyInvestment();
                currentShares += request.getMonthlyInvestment() / closePrice;
            }
            previousMonth = currentMonth;
            
            double portfolioValue = currentShares * closePrice;
            
            MonthlyData monthlyData = new MonthlyData();
            monthlyData.setDate(date);
            monthlyData.setInvestment(totalInvestment);
            monthlyData.setPortfolioValue(portfolioValue);
            monthlyDataList.add(monthlyData);
        }
        
        // 最終的な投資成果の計算
        double finalValue = monthlyDataList.get(monthlyDataList.size() - 1).getPortfolioValue();
        double totalReturn = ((finalValue - totalInvestment) / totalInvestment) * 100;
        
        // 年率リターンの計算
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        double years = ChronoUnit.DAYS.between(startLocalDate, endLocalDate) / 365.0;
        double annualizedReturn = (Math.pow(1 + totalReturn / 100, 1 / years) - 1) * 100;
        
        // シャープレシオの計算
        double sharpeRatio = calculateSharpeRatio(monthlyDataList, annualizedReturn);

        result.setTotalInvestment(totalInvestment);
        result.setFinalValue(finalValue);
        result.setTotalReturn(totalReturn);
        result.setAnnualizedReturn(annualizedReturn);
        result.setSharpeRatio(sharpeRatio);
        result.setMonthlyData(monthlyDataList);
        
        return result;
    }

    private double calculateSharpeRatio(List<MonthlyData> dailyData, double annualizedReturn) {
        if (dailyData == null || dailyData.size() < 2) {
            return 0.0;
        }

        // 日次リターンのリストを計算（キャッシュフローを考慮）
        List<Double> dailyReturns = IntStream.range(1, dailyData.size())
            .mapToObj(i -> {
                double previousValue = dailyData.get(i - 1).getPortfolioValue();
                double currentValue = dailyData.get(i).getPortfolioValue();
                double previousInvestment = dailyData.get(i - 1).getInvestment();
                double currentInvestment = dailyData.get(i).getInvestment();
                double cashFlow = currentInvestment - previousInvestment;
                
                if (previousValue == 0) {
                    return 0.0;
                }
                
                return (currentValue - previousValue - cashFlow) / previousValue;
            })
            .collect(Collectors.toList());

        if (dailyReturns.isEmpty()) {
            return 0.0;
        }

        // 日次リターンの標準偏差を計算
        double meanReturn = dailyReturns.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double standardDeviation = Math.sqrt(dailyReturns.stream()
            .mapToDouble(r -> Math.pow(r - meanReturn, 2))
            .average()
            .orElse(0.0));

        // 年率リスク（ボラティリティ）に変換
        double annualizedVolatility = standardDeviation * Math.sqrt(252);
        
        // 年率リスクが0の場合はシャープレシオを0とする
        if (annualizedVolatility == 0) {
            return 0.0;
        }

        // 年率リターン / 年率リスク でシャープレシオを計算
        return (annualizedReturn / 100.0) / annualizedVolatility;
    }
} 