CREATE TABLE free_account_numbers {
   type                  VARCHAR(255) NOT NULL,
   account_number        NUMERIC NOT NULL,
   CONSTRAINT pk_free_account_numbers PRIMARY KEY (type, account_number)
};

CREATE TABLE account_number_sequence {
   id                    SMALLSERIAL PRIMARY KEY,
   type                  VARCHAR(255) NOT NULL,
   current_number        NUMERIC NOT NULL,
};
