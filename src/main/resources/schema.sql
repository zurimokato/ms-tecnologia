CREATE TABLE IF NOT EXISTS tecnologias (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(90)  NOT NULL
);
