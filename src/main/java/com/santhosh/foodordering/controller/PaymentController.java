package com.santhosh.foodordering.controller;

import com.santhosh.foodordering.dto.request.PaymentRequest;
import com.santhosh.foodordering.dto.response.PaymentResponse;
import com.santhosh.foodordering.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Payment Management.
 *
 * CUSTOMER:
 * - POST (pay for an order)
 * - GET /{orderId} (view payment for their order)
 *
 * ADMIN:
 * - GET /{orderId} (view any payment)
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Process payment for an order.
     * The amount is always the order total (cannot under/over-pay).
     *  On success, the order status changes to PAID.
     *  Restaurant confirmation is performed separately.
     * CUSTOMER only.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> pay(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PaymentResponse.from(paymentService.pay(request)));
    }

    /**
     * Get payment details for a specific order.
     * CUSTOMER can see payments for their own orders.
     * ADMIN can see any payment.
     */
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public PaymentResponse getByOrder(@PathVariable Long orderId) {
        return PaymentResponse.from(paymentService.getByOrder(orderId));
    }
}

