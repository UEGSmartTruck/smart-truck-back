-- Migration V2: create users table
CREATE TABLE IF NOT EXISTS users (
       id VARCHAR(36) PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       phone VARCHAR(11) NOT NULL,
       email VARCHAR(100) NOT NULL,
       passwordHash VARCHAR(100) NOT NULL,
       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       deleted_at TIMESTAMP WITHOUT TIME ZONE NULL,
       login_at TIMESTAMP WITHOUT TIME ZONE NULL

);
