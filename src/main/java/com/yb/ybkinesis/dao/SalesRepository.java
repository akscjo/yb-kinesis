package com.yb.ybkinesis.dao;

import com.yb.ybkinesis.model.BoxPlotData;
import com.yb.ybkinesis.model.CategoryBreakdown;
import com.yb.ybkinesis.model.SalesData;
import com.yb.ybkinesis.model.StateBreakdown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalesRepository implements SalesRepositoryInterface {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public long getTotalSalesInPastHour() {
        //String sql = "SELECT COUNT(*) FROM yb_sales WHERE sale_time >= NOW() - INTERVAL '1 HOUR'";
        String sql = "SELECT COUNT(*) FROM yb_sales;";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public List<SalesData> getSalesTimeSeries(int hours) {
        String sql = "SELECT DATE_TRUNC('minute', sale_time) AS minute, " +
                "SUM(sale_price) AS total_amount " +
                "FROM yb_sales " +
                "WHERE sale_time >= NOW() - INTERVAL '" + hours + " HOUR' " +
//                "AND sale_time < DATE_TRUNC('minute', NOW()) " +
                "GROUP BY DATE_TRUNC('minute', sale_time) " +
                "ORDER BY minute;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new SalesData(
                rs.getTimestamp("minute").toInstant(),
                rs.getDouble("total_amount")
        ));
    }

    @Override
    public List<CategoryBreakdown> getCategoryBreakdown(int hours) {
        String sql = "SELECT product_category, COALESCE(SUM(quantity), 0) as total_quantity " +
                "FROM yb_sales " +
                "WHERE sale_time >= NOW() - INTERVAL '" + hours + " HOUR' " +
                "GROUP BY product_category";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CategoryBreakdown(
                        rs.getString("product_category"),
                        rs.getInt("total_quantity")
                )
        );
    }

    @Override
    public List<StateBreakdown> getSalesBreakdownByState(int hours) {
        String sql = "SELECT state_code, COALESCE(SUM(sale_price), 0) as total_sales " +
                "FROM yb_sales " +
                "WHERE sale_time >= NOW() - INTERVAL '" + hours + " HOUR' " +
                "GROUP BY state_code " +
                "ORDER BY total_sales DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new StateBreakdown(
                        rs.getString("state_code"),
                        rs.getDouble("total_sales")
                )
        );
    }


    @Override
    public BoxPlotData getBoxPlotData(int hours) {
        String sql = "SELECT " +
                "PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY sale_price) AS q1, " +
                "PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY sale_price) AS median, " +
                "PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY sale_price) AS q3, " +
                "MIN(sale_price) AS min, " +
                "MAX(sale_price) AS max " +
                "FROM yb_sales " +
                "WHERE sale_time >= NOW() - INTERVAL '" + hours + " HOUR'";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new BoxPlotData(
                rs.getDouble("min"),
                rs.getDouble("q1"),
                rs.getDouble("median"),
                rs.getDouble("q3"),
                rs.getDouble("max")
        ));
    }
}
