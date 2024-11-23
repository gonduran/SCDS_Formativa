-- Insertar usuario de prueba
INSERT INTO users (
    username, 
    email, 
    password, 
    nombre_completo, 
    user_type, 
    enabled,
    account_non_expired,
    account_non_locked,
    credentials_non_expired
) 
VALUES 
(
    'admin', 
    'admin@test.com', 
    '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 
    'Admin Test', 
    0, 
    true,
    true,
    true,
    true
),
(
    'user', 
    'user@test.com', 
    '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 
    'User Test', 
    1, 
    true,
    true,
    true,
    true
);