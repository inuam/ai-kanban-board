package com.pm.backend.hello;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test: boots the full Spring context on a real HTTP port,
 * matching the Part 2 success criteria - the static hello page loads at /
 * and the sample API endpoint returns the expected JSON.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class HelloControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void apiHelloReturnsJsonGreeting() {
        ResponseEntity<HelloResponse> response = restTemplate.getForEntity("/api/hello", HelloResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new HelloResponse("Hello, World!"));
    }

    @Test
    void rootServesStaticHelloPage() {
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isNotNull().satisfies(
                contentType -> assertThat(contentType.isCompatibleWith(MediaType.TEXT_HTML)).isTrue());
        assertThat(response.getBody()).contains("Hello, World!");
    }
}
