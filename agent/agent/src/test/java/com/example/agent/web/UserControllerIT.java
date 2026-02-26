package com.example.agent.web;

import com.example.agent.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Test
    void should_create_and_get_user() {
        // 1) Create a user
        String createUrl = "http://localhost:" + port + "/api/users?name=Alice&email=alice@example.com";
        ResponseEntity<User> createResp = rest.postForEntity(createUrl, null, User.class);

        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        User created = createResp.getBody();

        assertThat(created).isNotNull();
        assertThat(created.id()).isNotBlank();
        assertThat(created.name()).isEqualTo("Alice");
        assertThat(created.email()).isEqualTo("alice@example.com");

        // 2) Retrieve the user by id
        String getUrl = "http://localhost:" + port + "/api/users/" + created.id();
        ResponseEntity<User> getResp = rest.getForEntity(getUrl, User.class);

        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        User fetched = getResp.getBody();

        assertThat(fetched).isNotNull();
        assertThat(fetched.id()).isEqualTo(created.id());
        assertThat(fetched.name()).isEqualTo("Alice");
        assertThat(fetched.email()).isEqualTo("alice@example.com");
    }
}