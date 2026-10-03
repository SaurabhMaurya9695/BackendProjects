package com.backend.kafka.api;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderPublisher orderPublisher;

    public OrderController(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @PostMapping
    public ResponseEntity<OrderPublisher.OrderPublication> publishOrder(@RequestBody JsonNode orderEvent) {
        OrderPublisher.OrderPublication publication = orderPublisher.publish(orderEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(publication);
    }

    @ExceptionHandler(OrderPublisher.InvalidOrderException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrder(OrderPublisher.InvalidOrderException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(OrderPublisher.OrderPublishException.class)
    public ResponseEntity<ErrorResponse> handlePublishFailure(OrderPublisher.OrderPublishException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(exception.getMessage()));
    }

    private record ErrorResponse(String message) {

    }
}
