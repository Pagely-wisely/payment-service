package com.pagely.paymentservice.domain.model;

public enum PaymentStatus {
    READY,      // 결제 준비 (PG사 요청 전 초기 상태)
    COMPLETED,  // 결제 승인 완료
    CANCELLED,  // 환불 완료 (PG사 환불 처리 완료)
    FAILED      // 결제 실패 (PG사 승인 거절 또는 오류)
}
