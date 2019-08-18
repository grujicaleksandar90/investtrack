package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CommonUnauthorizedException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CommonUnauthorizedException(String message, Throwable cause) {
        super(message, cause);

    }

    public CommonUnauthorizedException(String message) {
        super(message);

    }

    public CommonUnauthorizedException(Throwable cause) {
        super(cause);

    }
}
