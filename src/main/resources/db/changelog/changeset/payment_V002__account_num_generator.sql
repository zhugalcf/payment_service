CREATE TABLE free_account_numbers (
   account_type          VARCHAR(255) NOT NULL,
   account_number        NUMERIC NOT NULL,
   CONSTRAINT pk_free_account_numbers PRIMARY KEY (account_type, account_number)
);

CREATE TABLE account_number_sequence (
   id                    SMALLSERIAL PRIMARY KEY,
   account_type          VARCHAR(255) NOT NULL,
   current_number        NUMERIC NOT NULL DEFAULT 0
);

INSERT INTO account_number_sequence (account_type, current_number)
VALUES ('8888', 0), ('9999', 0);
