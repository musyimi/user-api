package com.musyimi.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.musyimi.user.User;
import com.musyimi.user.UserRegistrationRequest;
import com.musyimi.user.UserUpdateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "10000")
public class UserIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void RegisterUser() {

        Faker faker = new Faker();
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        String phoneNumber = faker.phoneNumber().phoneNumber();
        String email = faker.internet().emailAddress();
        String residence = faker.address().fullAddress();

        UserRegistrationRequest request = new UserRegistrationRequest(
                firstName, lastName, phoneNumber, email, residence
        );

        webTestClient.post()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<User> allUsers = webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                })
                .returnResult()
                .getResponseBody();

        User expectedUser = new User(
                firstName, lastName, phoneNumber, email, residence
        );

        Assertions.assertThat(allUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedUser);

        int id = allUsers.stream()
                        .filter(user -> user.getEmail().equals(email))
                        .map(User::getId)
                        .findFirst()
                        .orElseThrow();

        expectedUser.setId(id);

         webTestClient.get()
                .uri("/api/v1/users" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<User>() {})
                .isEqualTo(expectedUser);
    }

    @Test
    void deleteUser() {

        Faker faker = new Faker();
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        String phoneNumber = faker.phoneNumber().phoneNumber();
        String email = faker.internet().emailAddress();
        String residence = faker.address().fullAddress();

        UserRegistrationRequest request = new UserRegistrationRequest(
                firstName, lastName, phoneNumber, email, residence
        );

        webTestClient.post()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<User> allUsers = webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                })
                .returnResult()
                .getResponseBody();


        int id = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri("/api/v1/users" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();


        webTestClient.get()
                .uri("/api/v1/users" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void updateUser() {
        Faker faker = new Faker();
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        String phoneNumber = faker.phoneNumber().phoneNumber();
        String email = faker.internet().emailAddress();
        String residence = faker.address().fullAddress();

        UserRegistrationRequest request = new UserRegistrationRequest(
                firstName, lastName, phoneNumber, email, residence
        );

        webTestClient.post()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<User> allUsers = webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                })
                .returnResult()
                .getResponseBody();


        int id = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();

        String newFirstName = "doku";
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                newFirstName, null, null, null, null
        );

        webTestClient.put()
                .uri("/api/v1/users" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), UserUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        User updatedUser = webTestClient.get()
                .uri("/api/v1/users" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();

        User expected = new User(
                id, newFirstName, lastName, phoneNumber, email, residence
        );

        Assertions.assertThat(updatedUser).isEqualTo(expected);
    }
}
