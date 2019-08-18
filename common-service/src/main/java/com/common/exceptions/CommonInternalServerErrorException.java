package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CommonInternalServerErrorException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CommonInternalServerErrorException(String message, Throwable cause) {
        super(message, cause);

    }

    public CommonInternalServerErrorException(String message) {
        super(message);

    }

    public CommonInternalServerErrorException(Throwable cause) {
        super(cause);

    }
}
