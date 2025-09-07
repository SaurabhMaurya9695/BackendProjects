package com.backend.design.pattern.designs.Zepto.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a delivery partner in the Zepto system.
 */
@Setter
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class DeliveryPartner {

    /**
     * Name of the delivery partner.
     */
    private String deliveryPartnerName;
}
