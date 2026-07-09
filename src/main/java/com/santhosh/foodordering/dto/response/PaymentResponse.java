package com.santhosh.foodordering.dto.response;

import com.santhosh.foodordering.model.Payment;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long payid,
        Long orderId,
        double amount,
        String paymentMode,
        String paymentStatus,
        LocalDateTime paidAt
) {
    public static PaymentResponse from(Payment p) {
        return new PaymentResponse(
                p.getPayid(),
                p.getOrder().getOrdid(),
                p.getAmount(),
                p.getPaymentMode().name(),
                p.getPaymentStatus().name(),
                p.getPaidAt()
        );
    }
}
