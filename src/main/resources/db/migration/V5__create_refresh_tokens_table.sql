CREATE TABLE refresh_tokens (
    token VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- índice para busca rápida por user_id
CREATE INDEX refresh_tokens_user_id_idx ON refresh_tokens(user_id);

-- índice para auxiliar na limpeza de tokens antigos
CREATE INDEX refresh_tokens_created_at_idx ON refresh_tokens(created_at);
