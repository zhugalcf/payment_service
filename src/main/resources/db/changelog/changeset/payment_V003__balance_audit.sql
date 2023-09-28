create table balance_audit(
id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
user_id bigint not null,
sender_balance_id bigint not null references balance(id),
getter_balance_id bigint not null references balance(id),
lock_value varchar(244) not NULL,
amount numeric not null,
currency varchar(30),
status integer not null check (status<3),
created timestamptz DEFAULT current_timestamp,
clear_scheduled_at timestamptz
);

CREATE UNIQUE INDEX balance_audit_unique_index
    ON balance_audit (lock_value, sender_balance_id, getter_balance_id)
    WHERE status = 0;