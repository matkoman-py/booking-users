CREATE TABLE IF NOT EXISTS t_user (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    residence VARCHAR(255),
    role VARCHAR(255) NOT NULL
);