package com.santhosh.foodordering.exception;

/** Thrown when creating something that already exists (e.g. duplicate username). Maps to HTTP 409. */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
