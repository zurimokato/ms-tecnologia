package com.bootcamp.tecnologia.domain.exception;

public final class DomainConstants {

    private DomainConstants() {
    }

    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_DESCRIPTION_LENGTH = 90;
    public static final String NAME_REQUIRED = "El nombre es obligatorio";
    public static final String DESCRIPTION_REQUIRED = "La descripción es obligatoria";
    public static final String NAME_TOO_LONG = "El nombre no puede exceder los 50 caracteres";
    public static final String DESCRIPTION_TOO_LONG = "La descripción no puede exceder los 90 caracteres";
}
