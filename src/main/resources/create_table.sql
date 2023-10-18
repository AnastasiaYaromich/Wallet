CREATE SCHEMA wallet;
CREATE SCHEMA migration;


CREATE TABLE wallet.users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(255),
    password VARCHAR(255),
    balance NUMERIC,
    role VARCHAR(255)
);

CREATE TABLE wallet.transaction (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(255),
    type VARCHAR(255),
    condition VARCHAR(255),
    note VARCHAR(255),
    user_id BIGINT
);


CREATE TABLE wallet.audit (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255),
    time VARCHAR(255),
    status VARCHAR(255),
    balance NUMERIC,
    note VARCHAR(255),
    user_id BIGINT
);


CREATE TABLE migration.DATABASECHANGELOG (
    ID INT PRIMARY KEY NOT NULL
);