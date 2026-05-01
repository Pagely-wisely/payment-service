package com.pagely.paymentservice.domain.model;

public enum SettlementStatus {
    PENDING,    // 정산 대기
    COMPLETED,  // 정산 완료
    CANCELLED   // 정산 취소
}
