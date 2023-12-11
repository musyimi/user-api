package com.musyimi.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDataAccessService implements UserDao{

    private static List<User> users;

    static {
        users = new ArrayList<>();
        User hamadi = new User(
                1,
                "Hamadi",
                "Kibindoni",
                "0700000000",
                "hamadikibindoni@gmail.com",
                "Ruaka"
        );
        users.add(hamadi);

        User Kaka = new User(
                2,
                "Kaka",
                "Zema",
                "070000220",
                "kakazema@gmail.com",
                "Kitui"
        );
        users.add(Kaka);
    }

    @Override
    public List<User> selectAllUsers() {
        return users;
    }

    @Override
    public Optional<User> selectUserById(Integer id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }
}
