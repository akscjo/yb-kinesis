package com.yb.ybkinesis.model;

public class BoxPlotData {
    private double min;
    private double q1;
    private double median;
    private double q3;
    private double max;


    // Constructor, getters, and setters
    public BoxPlotData(double min, double q1, double median, double q3, double max) {
        this.min = min;
        this.q1 = q1;
        this.median = median;
        this.q3 = q3;
        this.max = max;
    }

    // Getters and setters

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getQ1() {
        return q1;
    }

    public void setQ1(double q1) {
        this.q1 = q1;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getQ3() {
        return q3;
    }

    public void setQ3(double q3) {
        this.q3 = q3;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }




}

