package com.example.demo.model;

import java.util.List;

public class RevenueDTO {
    private double totalRevenue;
    private long totalOrders;
    private long totalProductsSold;
    private List<String> chartLabels; // Lưu ngày/tháng (Trục X)
    private List<Double> chartData;   // Lưu tiền tương ứng (Trục Y)

    // Getters và Setters
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public long getTotalProductsSold() { return totalProductsSold; }
    public void setTotalProductsSold(long totalProductsSold) { this.totalProductsSold = totalProductsSold; }
    public List<String> getChartLabels() { return chartLabels; }
    public void setChartLabels(List<String> chartLabels) { this.chartLabels = chartLabels; }
    public List<Double> getChartData() { return chartData; }
    public void setChartData(List<Double> chartData) { this.chartData = chartData; }
}