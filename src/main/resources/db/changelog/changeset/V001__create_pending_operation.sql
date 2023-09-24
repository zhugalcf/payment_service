CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE pending_operation (
    operation_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    operation_type SMALLINT NOT NULL,
    operation_status SMALLINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    user_id BIGINT NOT NULL,
    sender_account_id BIGINT NOT NULL,
    receiver_account_id BIGINT NOT NULL,
    currency SMALLINT NOT NULL,
    scheduled_at timestamptz NOT NULL,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL,
    version BIGINT NOT NULL
);