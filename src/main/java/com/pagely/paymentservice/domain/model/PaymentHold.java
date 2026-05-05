package com.pagely.paymentservice.domain.model;

import com.pagely.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "p_payment_hold")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHold extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "buyer_id", nullable = false)
    private UUID buyerId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentHoldStatus status;

    @Column(name = "held_at", nullable = false)
    private LocalDateTime heldAt;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    public static PaymentHold create(Payment payment) {
        PaymentHold paymentHold = new PaymentHold();
        paymentHold.payment = payment;
        paymentHold.orderId = payment.getOrderId();
        paymentHold.sellerId = payment.getSellerId();
        paymentHold.buyerId = payment.getBuyerId();
        paymentHold.amount = payment.getAmount();
        paymentHold.status = PaymentHoldStatus.HOLDING;
        paymentHold.heldAt = LocalDateTime.now();
        return paymentHold;
    }
}
