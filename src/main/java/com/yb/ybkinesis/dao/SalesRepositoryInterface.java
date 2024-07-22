package com.yb.ybkinesis.dao;

import com.yb.ybkinesis.model.CategoryBreakdown;
import com.yb.ybkinesis.model.SalesData;
import com.yb.ybkinesis.model.StateBreakdown;

import java.util.List;

public interface SalesRepositoryInterface {
    long getTotalSalesInPastHour();
    List<SalesData> getSalesTimeSeries(int hours);
    List<CategoryBreakdown> getCategoryBreakdown(int hours);
    List<StateBreakdown> getSalesBreakdownByState(int hours);
}
