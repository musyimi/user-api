package com.musyimi.user;

import com.musyimi.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends AbstractTestContainersUnitTest {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsUserByEmail() {
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        User user =  new User(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.phoneNumber().phoneNumber(),
                email,
                FAKER.address().fullAddress()
        );
        underTest.save(user);

        var actual = underTest.existsUserByEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existsUserByEmailWhenEmailNotFound() {
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();

        var actual = underTest.existsUserByEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void existsUserById() {

        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        User user =  new User(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.phoneNumber().phoneNumber(),
                email,
                FAKER.address().fullAddress()
        );
        underTest.save(user);

        int id = underTest.findAll()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();

          var actual = underTest.existsUserById(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existsUserByIdWhenIdNotFound() {

        int id = -1;

        var actual = underTest.existsUserById(id);

        assertThat(actual).isFalse();
    }
}