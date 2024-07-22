package com.yb.ybkinesis.model;

import java.time.Instant;

public class SalesData {
    private Instant minute;
    private double totalAmount;

    public SalesData(Instant minute, double totalAmount) {
        this.minute = minute;
        this.totalAmount = totalAmount;
    }

    public Instant getMinute() {
        return minute;
    }

    public void setMinute(Instant minute) {
        this.minute = minute;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
