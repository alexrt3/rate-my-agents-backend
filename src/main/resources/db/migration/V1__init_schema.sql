CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    phone_number VARCHAR(20),
    username VARCHAR(255),
    is_agent BOOLEAN,
    created_at TIMESTAMP DEFAULT NOW()
);