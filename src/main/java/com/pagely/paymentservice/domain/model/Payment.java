package com.pagely.paymentservice.domain.model;

import com.pagely.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "p_payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "buyer_id", nullable = false)
    private UUID buyerId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "method", length = 20)
    private String method;

    @Column(name = "pg_provider", length = 20)
    private String pgProvider;

    @Column(name = "payment_key", length = 200)
    private String paymentKey;

    @Column(name = "pg_approved_at")
    private LocalDateTime pgApprovedAt;

    @Column(name = "pg_cancelled_at")
    private LocalDateTime pgCancelledAt;

    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentHistory> histories = new ArrayList<>();

    public static Payment create(UUID orderId, UUID buyerId, int amount) {
        Payment payment = new Payment();
        payment.orderId = orderId;
        payment.buyerId = buyerId;
        payment.amount = amount;
        payment.status = PaymentStatus.READY;
        return payment;
    }
}
