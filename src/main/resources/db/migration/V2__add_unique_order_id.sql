ALTER TABLE p_payment
    ADD CONSTRAINT uq_payment_order_id UNIQUE (order_id);
