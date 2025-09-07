package com.backend.design.pattern.designs.Zepto.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a product in the Zepto system.
 */
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString
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
}
