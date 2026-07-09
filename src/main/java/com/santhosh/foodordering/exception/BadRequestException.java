package com.santhosh.foodordering.exception;

/** Thrown for invalid business operations (e.g. ordering from a closed restaurant). Maps to HTTP 400. */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
