-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255),
    user_type INTEGER DEFAULT 1,
    enabled BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE
);

-- Tabla de Recetas
CREATE TABLE IF NOT EXISTS recetas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    detalle_preparacion TEXT,
    tipo_cocina VARCHAR(100),
    pais_origen VARCHAR(100),
    tiempo_coccion VARCHAR(50),
    dificultad VARCHAR(50),
    imagen VARCHAR(255),
    valoracion_promedio DOUBLE
);

-- Tabla para ingredientes (relación many-to-many)
CREATE TABLE IF NOT EXISTS receta_ingredientes (
    receta_id BIGINT,
    ingrediente VARCHAR(255),
    FOREIGN KEY (receta_id) REFERENCES recetas(id)
);