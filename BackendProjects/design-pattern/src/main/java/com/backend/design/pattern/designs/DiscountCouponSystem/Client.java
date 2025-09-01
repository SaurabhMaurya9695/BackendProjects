package com.backend.design.pattern.designs.DiscountCouponSystem;

import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.ConcreteChains.BankingDiscount;
import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.ConcreteChains.BulkPurchaseDiscount;
import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.ConcreteChains.LoyaltyDiscount;
import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.ConcreteChains.SessionalDiscount;
import com.backend.design.pattern.designs.DiscountCouponSystem.Manager.CouponManager;
import com.backend.design.pattern.designs.DiscountCouponSystem.Models.Cart;
import com.backend.xjc.Product;

import java.util.List;

public class Client {

    public static void main(String[] args) {
        try {
            CouponManager mgr = CouponManager.getInstance();
            mgr.registerCoupon(new SessionalDiscount(10, "Clothing"));
            mgr.registerCoupon(new LoyaltyDiscount(5));
            mgr.registerCoupon(new BulkPurchaseDiscount(1000, 100));
            mgr.registerCoupon(new BankingDiscount("ABC", 2000, 15, 500));

            Product p1 = new Product();
            p1.setProductCategory("Clothing");
            p1.setProductName("Winter Jacket");
            p1.setProductPrice(1000);

            Product p2 = new Product();
            p2.setProductCategory("Smartphone");
            p2.setProductName("Electronics");
            p2.setProductPrice(20000);

            Product p3 = new Product();
            p3.setProductCategory("Jeans");
            p3.setProductName("Jeans");
            p3.setProductPrice(1000);

            Product p4 = new Product();
            p4.setProductCategory("Headphones");
            p4.setProductName("Electronics");
            p4.setProductPrice(2000);

            Cart cart = new Cart();
            cart.addProductToCart(p1, 1);
            cart.addProductToCart(p2, 1);
            cart.addProductToCart(p3, 2);
            cart.addProductToCart(p4, 1);
            cart.setLoyalMember(true);
            cart.setPaymentBank("ABC");

            System.out.println("Original Cart Total: " + cart.getOriginalTotal() + " Rs");

            List<String> applicable = mgr.getApplicable(cart);
            System.out.println("Applicable Coupons:");
            for (String name : applicable) {
                System.out.println(" - " + name);
            }

            double finalTotal = mgr.applyAll(cart);
            System.out.println("Final Cart Total after discounts: " + finalTotal + " Rs");
        } catch (Exception e) {
            System.out.println("Exception occured !!");
        }
    }
}
