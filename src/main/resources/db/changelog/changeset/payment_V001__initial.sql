CREATE TABLE account(
   id                           BIGSERIAL PRIMARY KEY,
   account_number               BIGINT NOT NULL UNIQUE
);

CREATE TABLE balance(
    id                          BIGSERIAL PRIMARY KEY,
    account_number              BIGINT NOT NULL UNIQUE,
    authorization_balance       NUMERIC NOT NULL,
    current_balance             NUMERIC NOT NULL,
    created                     timestamptz DEFAULT current_timestamp,
    updated                     timestamptz DEFAULT current_timestamp,
    balance_version             BIGINT NOT NULL,

    CONSTRAINT fk_account_number FOREIGN KEY (account_number) REFERENCES account (id)
);




