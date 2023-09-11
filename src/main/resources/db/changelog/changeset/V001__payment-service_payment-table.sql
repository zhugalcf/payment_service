CREATE TABLE IF NOT EXISTS payments (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    sender_account_id BIGINT NOT NULL,
    receiver_account_id BIGINT NOT NULL,
    currency varchar(3) NOT NULL,
    amount decimal NOT NULL,
    status varchar(11) NOT NULL,
    scheduled_at timestamptz,
    created_at timestamptz DEFAULT current_timestamp
)