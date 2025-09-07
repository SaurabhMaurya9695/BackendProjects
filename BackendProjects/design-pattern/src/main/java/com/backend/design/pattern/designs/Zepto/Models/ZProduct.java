package com.backend.design.pattern.designs.Zepto.Models;

/**
 * Represents a product in the Zepto system.
 */
public class ZProduct {

    /**
     * Unique SKU identifier for the product.
     */
    private int sku;

    /**
     * Name of the product.
     */
    private String productName;

    /**
     * Price of the product.
     */
    private double price;

    public ZProduct() {
    }

    public ZProduct(int sku, String productName, double price) {
        this.sku = sku;
        this.productName = productName;
        this.price = price;
    }

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
