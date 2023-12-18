package com.musyimi.user;

import com.musyimi.exception.DuplicateResourceException;
import com.musyimi.exception.RequestValidationException;
import com.musyimi.exception.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userDao);
    }


    @Test
    void getAllUsers() {
        underTest.getAllUsers();

        verify(userDao).selectAllUsers();
    }

    @Test
    void getUser() {
        int id = 20;

        User user = new User(
                id, "musa", "keys", "0741245688", "musa@keys.com", "Tanga"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        User actual = underTest.getUser(20);

        Assertions.assertThat(actual).isEqualTo(user);
    }

    @Test
    void willThrowWhenGetUserReturnsEmpty() {
        int id = 20;


        when(userDao.selectUserById(id)).thenReturn(Optional.empty());

         Assertions.assertThatThrownBy(() -> underTest.getUser(id))
                 .isInstanceOf(ResourceNotFoundException.class)
                 .hasMessage("User with id [%s] not found".formatted(id));
    }

    @Test
    void addUser() {
        String email = "wanyonyi@gmail.com";

        when(userDao.existsUserWithEmail(email)).thenReturn(false);

        UserRegistrationRequest request = new UserRegistrationRequest(
                "wanyonyi", "kama", "045785645", email, "Ngara"
        );

        underTest.addUser(request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userDao).insertUser(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        Assertions.assertThat(capturedUser.getId()).isNull();
        Assertions.assertThat(capturedUser.getFirst_name()).isEqualTo(request.first_name());
        Assertions.assertThat(capturedUser.getLast_name()).isEqualTo(request.last_name());
        Assertions.assertThat(capturedUser.getPhone_number()).isEqualTo(request.phone_number());
        Assertions.assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        Assertions.assertThat(capturedUser.getResidence()).isEqualTo(request.residence());

    }

    @Test
    void willThrowWhenEmailExistsWhileaddUser() {
        String email = "wanyonyi@gmail.com";

        when(userDao.existsUserWithEmail(email)).thenReturn(true);

        UserRegistrationRequest request = new UserRegistrationRequest(
                "wanyonyi", "kama", "045785645", email, "Ngara"
        );

        Assertions.assertThatThrownBy(() -> underTest.addUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already registered");


        verify(userDao, never()).insertUser(any());

    }

    @Test
    void deleteUserById() {
        int id = 20;

        when(userDao.existsUserWithId(id)).thenReturn(true);

        underTest.deleteUserById(id);

        verify(userDao).deleteUserById(id);
    }

    @Test
    void willThrowWhenDeleteUserByIdNotFound() {
        int id = 20;

        when(userDao.existsUserWithId(id)).thenReturn(false);

       Assertions.assertThatThrownBy(() -> underTest.deleteUserById(id))
                       .isInstanceOf(ResourceNotFoundException.class)
                               .hasMessage("User with id [%s] not found".formatted(id));

        verify(userDao, never()).deleteUserById(id);
    }

    @Test
    void updateUserEmail() {
        int id = 20;

        User user = new User(
                id, "musa", "keys", "0741245688", "musa@keys.com", "Tanga"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        String newEmail = "hamadi@mail.com";
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "musa", "keys", "0741245688",  newEmail, "Tanga"
        );

        when(userDao.existsUserWithEmail(newEmail)).thenReturn(false);

        underTest.updateUser(id, updateRequest);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userDao).updateUser(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        Assertions.assertThat(capturedUser.getFirst_name()).isEqualTo(user.getFirst_name());
        Assertions.assertThat(capturedUser.getLast_name()).isEqualTo(user.getLast_name());
        Assertions.assertThat(capturedUser.getPhone_number()).isEqualTo(user.getPhone_number());
        Assertions.assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(capturedUser.getResidence()).isEqualTo(user.getResidence());

    }

    @Test
    void willThrowWhenupdateUserEmailAlreadyTaken() {
        int id = 20;

        User user = new User(
                id, "musa", "keys", "0741245688", "musa@keys.com", "Tanga"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        String newEmail = "hamadi@mail.com";
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "musa", "keys", "0741245688",  newEmail, "Tanga"
        );

        when(userDao.existsUserWithEmail(newEmail)).thenReturn(true);

        Assertions.assertThatThrownBy(() ->underTest.updateUser(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already registered");


        verify(userDao, never()).updateUser(any());


    }

    @Test
    void updateUserFirstName() {
        int id = 20;

        User user = new User(
                id, "musa", "keys", "0741245688", "musa@keys.com", "Tanga"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        String newFirstName = "hamadi";
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                newFirstName, null, null,  null, null
        );


        underTest.updateUser(id, updateRequest);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userDao).updateUser(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        Assertions.assertThat(capturedUser.getFirst_name()).isEqualTo(newFirstName);
        Assertions.assertThat(capturedUser.getLast_name()).isEqualTo(user.getLast_name());
        Assertions.assertThat(capturedUser.getPhone_number()).isEqualTo(user.getPhone_number());
        Assertions.assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(capturedUser.getResidence()).isEqualTo(user.getResidence());

    }

    @Test
    void updateUserLastName() {
        int id = 20;

        User user = new User(
                id, "musa", "keys", "0741245688", "musa@keys.com", "Tanga"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "musa", "nyumba", "0741245688",  "musa@keys.com", "Tanga"
        );


        underTest.updateUser(id, updateRequest);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userDao).updateUser(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        Assertions.assertThat(capturedUser.getFirst_name()).isEqualTo(user.getFirst_name());
        Assertions.assertThat(capturedUser.getLast_name()).isEqualTo(user.getLast_name());
        Assertions.assertThat(capturedUser.getPhone_number()).isEqualTo(user.getPhone_number());
        Assertions.assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(capturedUser.getResidence()).isEqualTo(user.getResidence());

    }

    @Test
    void updateUserResidence() {
        int id = 20;

        User user = new User(
                id, "musa", "keys", "0741245688", "musa@keys.com", "Tanga"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "musa", "keys", "0741245688",  "musa@keys.com", "kisii"
        );


        underTest.updateUser(id, updateRequest);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userDao).updateUser(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        Assertions.assertThat(capturedUser.getFirst_name()).isEqualTo(user.getFirst_name());
        Assertions.assertThat(capturedUser.getLast_name()).isEqualTo(user.getLast_name());
        Assertions.assertThat(capturedUser.getPhone_number()).isEqualTo(user.getPhone_number());
        Assertions.assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(capturedUser.getResidence()).isEqualTo(user.getResidence());

    }

    @Test
    void willThrowWhenNochangesFound() {
        int id = 20;

        User user = new User(
                id, "musa", "keys", "0741245688", "musa@keys.com", "Tanga"
        );

        when(userDao.selectUserById(id)).thenReturn(Optional.of(user));

        UserUpdateRequest updateRequest = new UserUpdateRequest(
                user.getFirst_name(),
                user.getLast_name(),
                user.getPhone_number(),
                user.getEmail(),
                user.getResidence()
        );

       Assertions.assertThatThrownBy(() ->  underTest.updateUser(id, updateRequest))
                       .isInstanceOf(RequestValidationException.class)
                               .hasMessage("No changes found");

        verify(userDao, never()).updateUser(any());


    }
}