package com.pm.backend.hello;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test: exercises the controller directly, with no Spring context.
 */
class HelloControllerTest {

    @Test
    void helloReturnsGreetingMessage() {
        HelloController controller = new HelloController();

        HelloResponse response = controller.hello();

        assertThat(response.message()).isEqualTo("Hello, World!");
    }
}
