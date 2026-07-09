package com.santhosh.foodordering.repo;

import com.santhosh.foodordering.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_Ordid(Long orderId);

    boolean existsByOrder_Ordid(Long orderId);
}
