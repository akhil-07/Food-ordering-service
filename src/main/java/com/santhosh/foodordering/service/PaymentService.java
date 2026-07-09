package com.santhosh.foodordering.service;

import com.santhosh.foodordering.dto.request.PaymentRequest;
import com.santhosh.foodordering.exception.BadRequestException;
import com.santhosh.foodordering.exception.ResourceNotFoundException;
import com.santhosh.foodordering.model.Order;
import com.santhosh.foodordering.model.OrderStatus;
import com.santhosh.foodordering.model.Payment;
import com.santhosh.foodordering.model.PaymentStatus;
import com.santhosh.foodordering.repo.PaymentRepository;
import com.santhosh.foodordering.security.CurrentUserProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final CurrentUserProvider currentUser;

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService,
                          CurrentUserProvider currentUser) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.currentUser = currentUser;
    }

    /**
     * Simulates paying for an order.
     * The customer always pays the exact order total.
     * An order can only be paid once.
     * On successful payment, the order status becomes PAID.
     * Restaurant confirmation happens separately.
     */
    public Payment pay(PaymentRequest request) {
        Order order = orderService.findEntity(request.orderId());
        assertOwner(order);

        if (paymentRepository.existsByOrder_Ordid(order.getOrdid())) {
            throw new BadRequestException("Order " + order.getOrdid() + " is already paid");
        }
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new BadRequestException("Only PLACED orders can be paid (status: " + order.getStatus() + ")");
        }
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMode(request.paymentMode());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

// Payment successful
        order.setStatus(OrderStatus.PAID);

        paymentRepository.save(payment);

// Explicitly save the updated order
        orderService.markAsPaid(order);

        return payment;
    }

    @Transactional(readOnly = true)
    public Payment getByOrder(Long orderId) {
        Order order = orderService.findEntity(orderId);
        assertOwner(order);
        return paymentRepository.findByOrder_Ordid(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for order: " + orderId));
    }

    /** Only the order's customer (or an ADMIN) may pay for / view its payment. */
    private void assertOwner(Order order) {
        if (!currentUser.isAdmin() && !order.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this order's payment");
        }
    }
}
