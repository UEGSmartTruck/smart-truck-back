INSERT INTO users (
    id,
    name,
    phone,
    email,
    passwordHash,
    created_at,
    updated_at
) VALUES (
             '00000000-0000-0000-0000-000000000001',  -- id fixo para admin
             'admin',                                  -- nome
             '00000000000',                            -- telefone fictício
             'admin@smarttruck.com',                   -- email
             '$2b$10$BWhH12dPfEEL8ND61P9jZeq7kjy5kFfb.XX25JxcADGj5HFKOqO4e', -- senha: admin (BCrypt'$2b$10$BWhH12dPfEEL8ND61P9jZeq7kjy5kFfb.XX25JxcADGj5HFKOqO4e'
             NOW(),                                    -- created_at
             NOW()                                     -- updated_at
         );
