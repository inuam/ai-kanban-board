package com.pm.backend.hello;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test: boots the full Spring context on a real HTTP port and
 * confirms the sample API endpoint returns the expected JSON.
 *
 * The frontend's static export is only present under src/main/resources/static
 * when the Docker build copies it in (see the root Dockerfile's frontend-build
 * stage) - it isn't there for a plain `mvn test`. Coverage that / actually
 * serves the Kanban board lives in the Playwright suite (frontend/tests),
 * run against the Docker-served app per Part 3 of docs/PLAN.md.
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
    void rootReturnsNotFoundWithoutABundledFrontend() {
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
