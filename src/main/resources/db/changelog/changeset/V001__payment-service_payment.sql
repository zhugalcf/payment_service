CREATE TABLE payment
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    owner_account_id VARCHAR(20) NOT NULL,
    receiver_account_id VARCHAR(20) NOT NULL,
    status VARCHAR(32),
    amount NUMERIC NOT NULL,
    currency VARCHAR(16) NOT NULL,
    idempotency_key VARCHAR(64) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    clear_scheduled_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE outbox_payment
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    idempotency_key VARCHAR(64) NOT NULL,
    status VARCHAR(32)  NOT NULL,
    is_posted BOOLEAN DEFAULT FALSE
);

CREATE INDEX uuid_index ON payment(idempotency_key);
CREATE INDEX owner_receiver_index ON payment(owner_account_id, receiver_account_id);
CREATE INDEX status_index ON payment(status);
CREATE INDEX posted_index ON outbox_payment(is_posted);