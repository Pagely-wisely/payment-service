-- =============================================
-- Payment Service DDL (PostgreSQL)
-- =============================================

-- ENUM 타입 정의

-- 결제 상태
CREATE TYPE payment_status AS ENUM (
    'READY',      -- 결제 준비 (PG사 요청 전 초기 상태)
    'COMPLETED',  -- 결제 승인 완료
    'CANCELLED',  -- 환불 완료 (PG사 환불 처리 완료)
    'FAILED'      -- 결제 실패 (PG사 승인 거절 또는 오류)
);

-- 결제 보관 상태
CREATE TYPE payment_hold_status AS ENUM (
    'HOLDING',   -- 보관
    'SETTLED',   -- 정산
    'REFUNDED'   -- 환불
);

-- 정산 상태
CREATE TYPE settlement_status AS ENUM (
    'PENDING',    -- 정산 대기
    'COMPLETED',  -- 정산 완료
    'CANCELLED'   -- 정산 취소
);

-- =============================================

-- 결제
CREATE TABLE p_payment
(
    id               UUID            NOT NULL,
    order_id         UUID            NOT NULL,
    buyer_id         UUID            NOT NULL,
    seller_id        UUID            NOT NULL,
    amount           INT             NOT NULL,
    status           payment_status  NOT NULL,
    method           VARCHAR(20),
    pg_provider      VARCHAR(20),
    payment_key      VARCHAR(200),
    pg_approved_at   TIMESTAMP,
    pg_cancelled_at  TIMESTAMP,
    cancel_reason    VARCHAR(200),
    created_at       TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by       UUID            NOT NULL,
    updated_at       TIMESTAMP,
    updated_by       UUID,
    deleted_at       TIMESTAMP,
    deleted_by       UUID,
    PRIMARY KEY (id)
);

COMMENT ON TABLE  p_payment                 IS '결제';
COMMENT ON COLUMN p_payment.id              IS '결제 ID';
COMMENT ON COLUMN p_payment.order_id        IS '주문 ID';
COMMENT ON COLUMN p_payment.buyer_id        IS '구매자 ID';
COMMENT ON COLUMN p_payment.seller_id       IS '판매자 ID';
COMMENT ON COLUMN p_payment.amount          IS '결제 금액';
COMMENT ON COLUMN p_payment.status          IS '결제 상태';
COMMENT ON COLUMN p_payment.method          IS '결제 수단 CARD/POINT';
COMMENT ON COLUMN p_payment.pg_provider     IS 'PG사';
COMMENT ON COLUMN p_payment.payment_key     IS 'PG측 결제 키';
COMMENT ON COLUMN p_payment.pg_approved_at  IS 'PG 승인 일시';
COMMENT ON COLUMN p_payment.pg_cancelled_at IS 'PG 환불 처리 일시';
COMMENT ON COLUMN p_payment.cancel_reason   IS '취소 사유';
COMMENT ON COLUMN p_payment.created_at      IS '생성 시각';
COMMENT ON COLUMN p_payment.created_by      IS '생성자';
COMMENT ON COLUMN p_payment.updated_at      IS '수정 시각';
COMMENT ON COLUMN p_payment.updated_by      IS '수정자';
COMMENT ON COLUMN p_payment.deleted_at      IS '삭제 시각';
COMMENT ON COLUMN p_payment.deleted_by      IS '삭제자';

-- =============================================

-- 결제 이력
CREATE TABLE p_payment_history
(
    id          UUID           NOT NULL,
    payment_id  UUID           NOT NULL,
    from_status payment_status NOT NULL,
    to_status   payment_status NOT NULL,
    reason      VARCHAR(200),
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
    created_by  UUID           NOT NULL,
    updated_at  TIMESTAMP,
    updated_by  UUID,
    deleted_at  TIMESTAMP,
    deleted_by  UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_payment_history_payment FOREIGN KEY (payment_id) REFERENCES p_payment (id)
);

COMMENT ON TABLE  p_payment_history             IS '결제 이력';
COMMENT ON COLUMN p_payment_history.id          IS '결제 이력 ID';
COMMENT ON COLUMN p_payment_history.payment_id  IS '결제 ID';
COMMENT ON COLUMN p_payment_history.from_status IS '이전 상태';
COMMENT ON COLUMN p_payment_history.to_status   IS '변경 상태';
COMMENT ON COLUMN p_payment_history.reason      IS '변경 사유';
COMMENT ON COLUMN p_payment_history.created_at  IS '생성 시각';
COMMENT ON COLUMN p_payment_history.created_by  IS '생성자';
COMMENT ON COLUMN p_payment_history.updated_at  IS '수정 시각';
COMMENT ON COLUMN p_payment_history.updated_by  IS '수정자';
COMMENT ON COLUMN p_payment_history.deleted_at  IS '삭제 시각';
COMMENT ON COLUMN p_payment_history.deleted_by  IS '삭제자';

-- =============================================

-- 결제 보류금
CREATE TABLE p_payment_hold
(
    id         UUID                NOT NULL,
    payment_id UUID                NOT NULL,
    order_id   UUID                NOT NULL,
    seller_id  UUID                NOT NULL,
    buyer_id   UUID                NOT NULL,
    amount     INT                 NOT NULL,
    status     payment_hold_status NOT NULL,
    held_at    TIMESTAMP           NOT NULL,
    settled_at TIMESTAMP,
    created_at TIMESTAMP           NOT NULL DEFAULT NOW(),
    created_by UUID                NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,
    deleted_at TIMESTAMP,
    deleted_by UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_payment_hold_payment FOREIGN KEY (payment_id) REFERENCES p_payment (id)
);

COMMENT ON TABLE  p_payment_hold            IS '결제 보류금';
COMMENT ON COLUMN p_payment_hold.id         IS '보류금 ID';
COMMENT ON COLUMN p_payment_hold.payment_id IS '결제 ID';
COMMENT ON COLUMN p_payment_hold.order_id   IS '주문 ID';
COMMENT ON COLUMN p_payment_hold.seller_id  IS '판매자 ID';
COMMENT ON COLUMN p_payment_hold.buyer_id   IS '구매자 ID';
COMMENT ON COLUMN p_payment_hold.amount     IS '보관 금액';
COMMENT ON COLUMN p_payment_hold.status     IS '결제 보관 상태';
COMMENT ON COLUMN p_payment_hold.held_at    IS '보관 시작 시각';
COMMENT ON COLUMN p_payment_hold.settled_at IS '정산 완료 시각';
COMMENT ON COLUMN p_payment_hold.created_at IS '생성 시각';
COMMENT ON COLUMN p_payment_hold.created_by IS '생성자';
COMMENT ON COLUMN p_payment_hold.updated_at IS '수정 시각';
COMMENT ON COLUMN p_payment_hold.updated_by IS '수정자';
COMMENT ON COLUMN p_payment_hold.deleted_at IS '삭제 시각';
COMMENT ON COLUMN p_payment_hold.deleted_by IS '삭제자';

-- =============================================

-- 정산
CREATE TABLE p_settlement
(
    id                UUID              NOT NULL,
    payment_hold_id   UUID              NOT NULL,
    seller_id         UUID              NOT NULL,
    order_id          UUID              NOT NULL,
    sale_amount       INT               NOT NULL,
    commission_rate   DECIMAL(4, 2)     NOT NULL,
    commission_amount INT               NOT NULL,
    settled_amount    INT               NOT NULL,
    status            settlement_status NOT NULL,
    settled_at        TIMESTAMP,
    created_at        TIMESTAMP         NOT NULL DEFAULT NOW(),
    created_by        UUID              NOT NULL,
    updated_at        TIMESTAMP,
    updated_by        UUID,
    deleted_at        TIMESTAMP,
    deleted_by        UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_settlement_payment_hold FOREIGN KEY (payment_hold_id) REFERENCES p_payment_hold (id)
);

COMMENT ON TABLE  p_settlement                   IS '정산';
COMMENT ON COLUMN p_settlement.id                IS '정산 ID';
COMMENT ON COLUMN p_settlement.payment_hold_id   IS '결제 보류금 ID';
COMMENT ON COLUMN p_settlement.seller_id         IS '판매자 ID';
COMMENT ON COLUMN p_settlement.order_id          IS '주문 ID';
COMMENT ON COLUMN p_settlement.sale_amount       IS '판매 금액';
COMMENT ON COLUMN p_settlement.commission_rate   IS '수수료율';
COMMENT ON COLUMN p_settlement.commission_amount IS '수수료 금액';
COMMENT ON COLUMN p_settlement.settled_amount    IS '실 지급액';
COMMENT ON COLUMN p_settlement.status            IS '정산 상태';
COMMENT ON COLUMN p_settlement.settled_at        IS '정산 완료 시각';
COMMENT ON COLUMN p_settlement.created_at        IS '생성 시각';
COMMENT ON COLUMN p_settlement.created_by        IS '생성자';
COMMENT ON COLUMN p_settlement.updated_at        IS '수정 시각';
COMMENT ON COLUMN p_settlement.updated_by        IS '수정자';
COMMENT ON COLUMN p_settlement.deleted_at        IS '삭제 시각';
COMMENT ON COLUMN p_settlement.deleted_by        IS '삭제자';
