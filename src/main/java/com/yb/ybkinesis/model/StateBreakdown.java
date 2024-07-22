package com.yb.ybkinesis.model;

public class StateBreakdown {
    private String stateCode;
    private double totalSales;

    // Constructor
    public StateBreakdown(String stateCode, double totalSales) {
        this.stateCode = stateCode;
        this.totalSales = totalSales;
    }

    // Getters and Setters
    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
}

