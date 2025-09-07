package com.backend.design.pattern.designs.Zepto.Models;

/**
 * Represents a delivery partner in the Zepto system.
 */
public class DeliveryPartner {

    /**
     * Name of the delivery partner.
     */
    private String deliveryPartnerName;

    public DeliveryPartner(String deliveryPartnerName) {
        this.deliveryPartnerName = deliveryPartnerName;
    }

    public String getDeliveryPartnerName() {
        return deliveryPartnerName;
    }

    public void setDeliveryPartnerName(String deliveryPartnerName) {
        this.deliveryPartnerName = deliveryPartnerName;
    }
}
