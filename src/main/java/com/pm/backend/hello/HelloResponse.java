package com.pm.backend.hello;

/**
 * JSON payload returned by GET /api/hello.
 */
public record HelloResponse(String message) {
}
