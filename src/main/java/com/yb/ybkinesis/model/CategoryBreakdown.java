package com.yb.ybkinesis.model;

public class CategoryBreakdown {
    private String productCategory;
    private int totalQuantity;

    public CategoryBreakdown(String productCategory, int totalQuantity) {
        this.productCategory = productCategory;
        this.totalQuantity = totalQuantity;
    }

    // Getters and setters
    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}

