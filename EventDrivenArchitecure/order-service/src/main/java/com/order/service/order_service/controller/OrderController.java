package com.order.service.order_service.controller;

import com.order.service.order_service.dto.OrderDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    // Dummy GET endpoint to fetch all orders
    @GetMapping
    public String getAllOrders() {
        return "Fetching all orders...";
    }

    // Dummy GET endpoint to fetch a single order by ID
    @GetMapping("/{orderId}")
    public String getOrderById(@PathVariable String orderId) {
        return "Fetching order with ID: " + orderId;
    }

    // Dummy POST endpoint to create a new order
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody String orderRequest) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setUserId(UUID.randomUUID().toString());
        orderDetails.setOrderId(UUID.randomUUID().toString());
        orderDetails.setUserEmailId("saurabh@gmail.com");
        orderDetails.setUserPhoneNo("981261986391");

        // now we have order ready we need to notify now
        notifytoNotificationService();
        return new ResponseEntity<>("order created Successfully", HttpStatus.CREATED);
    }

    private void notifytoNotificationService() {
        // we need to send notification to broker and broker is consumed by notificationService

    }
}
