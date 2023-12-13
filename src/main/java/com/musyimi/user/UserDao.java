package com.musyimi.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> selectAllUsers();
    Optional<User> selectUserById(Integer id);
    void insertUser(User user);
    boolean existsUserWithEmail(String email);
    boolean existsUserWithId(Integer id);
    void deleteUserById(Integer userId);
    void updateUser(User update);
}
