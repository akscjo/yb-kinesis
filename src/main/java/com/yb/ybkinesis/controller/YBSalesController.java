package com.yb.ybkinesis.controller;

import com.yb.ybkinesis.dao.SalesRepository;
import com.yb.ybkinesis.model.BoxPlotData;
import com.yb.ybkinesis.model.CategoryBreakdown;
import com.yb.ybkinesis.model.SalesData;
import com.yb.ybkinesis.model.StateBreakdown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class YBSalesController {
    @Autowired
    private SalesRepository salesRepository;

    @GetMapping("/api/test")
    public String getTest(){
        return ""+salesRepository.getTotalSalesInPastHour();
    }

    @GetMapping("/api/sales-ts")
    public List<SalesData> getSalesTimeSeries(@RequestParam(defaultValue = "4") int hours) {
        return salesRepository.getSalesTimeSeries(hours);
    }

    @GetMapping("/api/category-breakdown")
    public List<CategoryBreakdown> getCategoryBreakdown(@RequestParam(defaultValue = "4") int hours) {
        return salesRepository.getCategoryBreakdown(hours);
    }

    @GetMapping("/api/sales-breakdown-by-state")
    public List<StateBreakdown> getSalesBreakdownByState(@RequestParam(value = "hours", defaultValue = "4") int hours) {
        return salesRepository.getSalesBreakdownByState(hours);
    }

    @GetMapping("/api/box-plot-data")
    public BoxPlotData getBoxPlotData(@RequestParam(defaultValue = "4") int hours) {
        return salesRepository.getBoxPlotData(hours);
    }


}
