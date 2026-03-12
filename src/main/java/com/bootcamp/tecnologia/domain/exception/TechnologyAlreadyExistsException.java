package com.bootcamp.tecnologia.domain.exception;

public class TechnologyAlreadyExistsException extends RuntimeException {

    public TechnologyAlreadyExistsException(String name) {
        super("La tecnología con nombre '" + name + "' ya existe");
    }
}
