package com.bootcamp.tecnologia.domain.exception;

public class TechnologyNotFoundException extends RuntimeException {

    public TechnologyNotFoundException(Long id) {
        super("No se encontró la tecnología con id: " + id);
    }

    public TechnologyNotFoundException(String name) {
        super("No se encontró la tecnología con nombre: " + name);
    }
}
