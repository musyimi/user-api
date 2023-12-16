package com.musyimi.user;

import com.musyimi.exception.DuplicateResourceException;
import com.musyimi.exception.RequestValidationException;
import com.musyimi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(@Qualifier("jdbc") UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    public User getUser(Integer id) {
        return userDao.selectUserById(id)
                .orElseThrow(
                () -> new ResourceNotFoundException("User with id [%s] not found".formatted(id)));
    }

    public void addUser(UserRegistrationRequest userRegistrationRequest){

        String email = userRegistrationRequest.email();
        if(userDao.existsUserWithEmail(email)){
           throw new DuplicateResourceException(
                   "email already registered"
           );
        }
        User user = new User(
                userRegistrationRequest.first_name(),
                userRegistrationRequest.last_name(),
                userRegistrationRequest.phone_number(),
                userRegistrationRequest.email(),
                userRegistrationRequest.residence()
        );
        userDao.insertUser( user );
    }

    public void deleteUserById(Integer userId) {
        if( !userDao.existsUserWithId(userId)) {
            throw new ResourceNotFoundException(
                    "User with id [%s] not found".formatted(userId)
            );
        }

        userDao.deleteUserById(userId);
    }

    public void updateUser(
            Integer userId,
            UserUpdateRequest updateRequest
    ){
        User user = getUser(userId);

        boolean changes = false;

        if(updateRequest.first_name() != null && !updateRequest.first_name().equals(user.getFirst_name())) {
            user.setFirst_name(updateRequest.first_name());
            changes = true;
        }

        if(updateRequest.last_name()!= null && !updateRequest.last_name().equals(user.getLast_name())) {
            user.setFirst_name(updateRequest.last_name());
            changes = true;
        }

        if(updateRequest.phone_number() != null && !updateRequest.phone_number().equals(user.getPhone_number())){
            user.setFirst_name(updateRequest.phone_number());
            changes = true;
        }
        if(updateRequest.email() != null && !updateRequest.email().equals(user.getEmail())) {
           if(userDao.existsUserWithEmail(updateRequest.email())) {
               throw new DuplicateResourceException("Email already registered");
           }
           user.setEmail(updateRequest.email());
            changes = true;
        }
        if(updateRequest.residence() != null && !updateRequest.residence().equals(user.getResidence())) {
            user.setFirst_name(updateRequest.residence());
            changes = true;
        }

        if(!changes) {
            throw new RequestValidationException("No changes found");
        }
        userDao.updateUser(user);
    }
}
