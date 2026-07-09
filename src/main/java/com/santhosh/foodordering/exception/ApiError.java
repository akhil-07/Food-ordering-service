package com.santhosh.foodordering.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Uniform error body returned for every failed request, e.g.
 * <pre>
 * {
 *   "timestamp": "2026-06-10T12:00:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Restaurant not found with id: 5",
 *   "path": "/api/restaurants/5",
 *   "fieldErrors": [ { "field": "price", "message": "must be greater than 0" } ]
 * }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldValidationError> fieldErrors
) {
    public record FieldValidationError(String field, String message) {
    }
}
