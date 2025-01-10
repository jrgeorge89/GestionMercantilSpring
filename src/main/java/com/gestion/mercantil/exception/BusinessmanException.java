
package com.gestion.mercantil.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessmanException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private HttpStatus httpStatus;
    private String details;

    public BusinessmanException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessmanException(String message, HttpStatus httpStatus, String details) {
        super(message);
        this.httpStatus = httpStatus;
        this.details = details;
    }

    public BusinessmanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessmanException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
