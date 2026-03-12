-- =============================================================
-- Base de datos: db_tecnologia
-- Microservicio: ms-tecnologia
-- =============================================================

CREATE DATABASE IF NOT EXISTS db_tecnologia;
USE db_tecnologia;

CREATE TABLE IF NOT EXISTS tecnologias (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(90)  NOT NULL,
    CONSTRAINT chk_nombre_not_empty CHECK (CHAR_LENGTH(TRIM(nombre)) > 0),
    CONSTRAINT chk_descripcion_not_empty CHECK (CHAR_LENGTH(TRIM(descripcion)) > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índice para búsquedas por nombre (ya cubierto por UNIQUE, pero explícito para claridad)
-- CREATE INDEX idx_tecnologias_nombre ON tecnologias(nombre);

-- Datos de prueba (opcional)
INSERT INTO tecnologias (nombre, descripcion) VALUES
    ('Java', 'Lenguaje de programación orientado a objetos multiplataforma'),
    ('Spring Boot', 'Framework para crear aplicaciones Java empresariales'),
    ('Angular', 'Framework de frontend desarrollado por Google'),
    ('Git', 'Sistema de control de versiones distribuido'),
    ('Node.js', 'Entorno de ejecución de JavaScript del lado del servidor'),
    ('Mockito', 'Framework de testing para mocks en Java'),
    ('JUnit', 'Framework de pruebas unitarias para Java');
