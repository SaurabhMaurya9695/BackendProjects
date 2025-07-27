package com.notification.service.functions;

import com.notification.service.dto.OrderDto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class NotificationService {

    // It decouples the logic , this func can run as a web-endpoint , task etc ..
    @Bean
    public Supplier<String> testing() {
        return () -> "This is testing";
    }

    @Bean
    public Function<String, String> sayHello() {
        return (message) -> "calling sayHello function and msg is " + message;
    }

    @Bean
    public Function<OrderDto, String> orderNotification() {
        return orderDto -> {
            // Log the order details
            System.out.println("Received order for notification: " + orderDto);

            // Send notification (email/SMS/Push etc.)
            sendNotification(orderDto);

            // Returning user's phone number as a simple response
            return orderDto.getUserPhoneNo();
        };
    }

    private void sendNotification(OrderDto orderDto) {
        // Example logic
        System.out.println("Sending notification to user:");
        System.out.println("Email: " + orderDto.getUserEmailId());
        System.out.println("Phone: " + orderDto.getUserPhoneNo());
        System.out.println("Order ID: " + orderDto.getOrderId());
        System.out.println("Price: ₹" + orderDto.getPrice());
    }
}
