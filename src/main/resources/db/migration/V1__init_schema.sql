CREATE SEQUENCE IF NOT EXISTS battery_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;

CREATE TABLE IF NOT EXISTS battery (
                                       id            BIGINT PRIMARY KEY,
    name          VARCHAR(512) NOT NULL UNIQUE,
    postcode      INTEGER NOT NULL,
    watt_capacity BIGINT NOT NULL,
    created_at    TIMESTAMP NOT NULL,
    modified_at   TIMESTAMP NOT NULL
    );

ALTER SEQUENCE battery_id_seq OWNED BY battery.id;

CREATE INDEX IF NOT EXISTS idx_battery_postcode ON battery(postcode);
