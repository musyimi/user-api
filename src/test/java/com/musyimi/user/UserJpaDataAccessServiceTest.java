package com.musyimi.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class UserJpaDataAccessServiceTest {
    
    private UserJpaDataAccessService underTest;
    private AutoCloseable autoCloseable;
    
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserJpaDataAccessService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllUsers() {
        underTest.selectAllUsers();

       verify(userRepository).findAll();
    }



    @Test
    void selectUserById() {
        int id = 1;

        underTest.selectUserById(id);

        verify(userRepository).findById(id);
    }

    @Test
    void insertUser() {
        User user = new User(
                1, "Mama", "Yao", "074587520", "mamayao@mail.com", "Kitui"
        );

        underTest.insertUser(user);

        verify(userRepository).save(user);
    }

    @Test
    void existsUserWithEmail() {
        String email = "lala@mail.com";

        underTest.existsUserWithEmail(email);

        verify(userRepository).existsUserByEmail(email);
    }

    @Test
    void existsUserWithId() {
        int id = 1;

        underTest.existsUserWithId(id);

        verify(userRepository).existsUserById(id);
    }

    @Test
    void deleteUserById() {
        int id = 1;

        underTest.deleteUserById(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void updateUser() {
        User user = new User(
                1, "kamau", "kalili", "04578256", "kama@mail.com", "Kahawa"
        );

        underTest.updateUser(user);

        verify(userRepository).save(user);
    }
}