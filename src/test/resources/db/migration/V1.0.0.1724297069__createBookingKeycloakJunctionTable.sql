CREATE TABLE booking_keycloak_user (
    booking_id VARCHAR(255) NOT NULL,
    keycloak_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (booking_id, keycloak_id)
);