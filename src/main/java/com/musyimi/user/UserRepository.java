package com.musyimi.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByEmail(String email);
    boolean existsUserById(Integer id);
}
