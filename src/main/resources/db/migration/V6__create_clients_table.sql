-- Migration V6: create clients table (Sem Soft Delete)
CREATE TABLE IF NOT EXISTS clients (
                                       id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL
    );
