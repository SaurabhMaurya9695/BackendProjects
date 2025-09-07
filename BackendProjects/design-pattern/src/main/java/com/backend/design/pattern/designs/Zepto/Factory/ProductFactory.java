package com.backend.design.pattern.designs.Zepto.Factory;

import com.backend.design.pattern.designs.Zepto.Models.ZProduct;

/**
 * Factory class to create {@link ZProduct} instances
 * based on predefined SKU mappings.
 */
public class ProductFactory {

    /**
     * Creates a {@link ZProduct} with details (name, price)
     * corresponding to the given SKU.
     *
     * @param sku the product SKU
     * @return a {@link ZProduct} instance, or a product with only SKU set if unknown
     */
    public static ZProduct createProduct(int sku) {
        ZProduct zProduct = new ZProduct();
        String productName = null;
        double price = 0;

        if (sku == 101) {
            productName = "T-Shirt";
            price = 100.00;
        } else if (sku == 102) {
            productName = "Lower";
            price = 50.00;
        } else if (sku == 103) {
            productName = "Jeans";
            price = 1500.00;
        } else if (sku == 201) {
            productName = "Shoes";
            price = 3700.00;
        } else if (sku == 202) {
            productName = "TV";
            price = 35000.00;
        } else if (sku == 203) {
            productName = "Cooler";
            price = 4000.00;
        }

        if (productName != null) {
            zProduct.setProductName(productName);
            zProduct.setPrice(price);
            zProduct.setSku(sku);
        }

        return zProduct;
    }
}
