package com.pm.backend.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Minimal REST endpoint used to confirm the backend is up and reachable.
 * The static "hello world" page at / is served directly from
 * src/main/resources/static/index.html by Spring Boot's default static
 * resource handling - no controller needed for that.
 */
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public HelloResponse hello() {
        return new HelloResponse("Hello, World!");
    }
}
