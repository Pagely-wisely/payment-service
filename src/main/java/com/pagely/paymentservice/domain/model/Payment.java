package com.pagely.paymentservice.domain.model;

import com.pagely.common.entity.BaseEntity;
import com.pagely.common.exception.BusinessException;
import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;
import com.pagely.paymentservice.domain.exception.PaymentErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Column(name = "buyer_id", nullable = false)
    private UUID buyerId;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

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

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentHold paymentHold;

    public static Payment create(UUID orderId, UUID buyerId, UUID sellerId, int amount) {
        Payment payment = new Payment();
        payment.orderId = orderId;
        payment.buyerId = buyerId;
        payment.sellerId = sellerId;
        payment.amount = amount;
        payment.status = PaymentStatus.READY;
        return payment;
    }

    public void validateAmount(int amount) {
        if (this.amount != amount) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }
    }

    public void validateConfirmResult(PaymentProviderConfirmResult result) {
        if (result == null) {
            throw new BusinessException(PaymentErrorCode.PG_CONFIRM_FAILED);
        }

        UUID orderId = UUID.fromString(result.orderId());

        if (!this.orderId.equals(orderId)) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_ORDER_ID_MISMATCH);
        }

        if (this.amount != result.totalAmount()) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }
    }

    public void confirm(PaymentProviderConfirmResult result) {
        if (result == null) {
            throw new BusinessException(PaymentErrorCode.PG_CONFIRM_FAILED);
        }

        PaymentStatus prevStatus = this.status;
        this.status = PaymentStatus.COMPLETED;
        this.method = result.method();
        this.paymentKey = result.paymentKey();
        this.pgApprovedAt = result.approvedAt();

        // 결제 이력 등록
        this.histories.add(PaymentHistory.of(this, prevStatus, "PG 결제 승인"));

        // 결제 보류금 저장
        this.paymentHold = PaymentHold.create(this);
    }

    public void validateBuyer(UUID userId) {
        if (!this.buyerId.equals(userId)) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_BUYER_MISMATCH);
        }
    }

    public void markConfirmRequested() {
        if (this.status != PaymentStatus.READY) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_NOT_CONFIRMABLE);
        }
        this.status = PaymentStatus.CONFIRM_REQUESTED;
    }

    public void markFailed(String reason) {
        if (this.status == PaymentStatus.COMPLETED) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_ALREADY_COMPLETED);
        }

        if (this.status == PaymentStatus.FAILED) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_ALREADY_CANCELLED);
        }

        PaymentStatus prevStatus = this.status;
        this.status = PaymentStatus.FAILED;
        this.histories.add(PaymentHistory.of(this, prevStatus, reason));
    }

    public boolean isCompleted() {
        return this.status == PaymentStatus.COMPLETED;
    }
}
