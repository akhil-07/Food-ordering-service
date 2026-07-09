package com.santhosh.foodordering.exception;

/** Thrown when an entity referenced by id (or unique key) does not exist. Maps to HTTP 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    /** Convenience: "Restaurant not found with id: 5". */
    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " not found with id: " + id);
    }
}
