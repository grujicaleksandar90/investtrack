package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommonNotFoundException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CommonNotFoundException(String message, Throwable cause) {
        super(message, cause);

    }

    public CommonNotFoundException(String message) {
        super(message);

    }

    public CommonNotFoundException(Throwable cause) {
        super(cause);

    }
}
