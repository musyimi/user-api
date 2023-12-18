package com.musyimi.user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class UserJpaDataAccessService implements UserDao{

    private final UserRepository userRepository;

    public UserJpaDataAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> selectAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> selectUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsUserWithEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existsUserWithId(Integer id) {
        return userRepository.existsUserById(id);
    }

    @Override
    public void deleteUserById(Integer userId) {
       userRepository.deleteById(userId);
    }


    @Override
    public void updateUser(User update) {
        userRepository.save(update);
    }
}
