package com.musyimi.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class UserJdbcDataAccessService implements UserDao{

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public UserJdbcDataAccessService(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<User> selectAllUsers() {
        var sql = """
                SELECT id, first_name, last_name, phone_number, email, residence
                FROM "user"
                """;

        return jdbcTemplate.query(sql, userRowMapper);

    }

    @Override
    public Optional<User> selectUserById(Integer id) {
        var sql = """
                SELECT id, first_name, last_name, phone_number, email, residence
                FROM "user"
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, userRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertUser(User user) {
       var sql = """
               INSERT INTO "user"(first_name, last_name, phone_number, email, residence)
               VALUES (?, ?, ? ,?, ?)
               """;
       jdbcTemplate.update(
               sql,
               user.getFirst_name(),
               user.getLast_name(),
               user.getPhone_number(),
               user.getEmail(),
               user.getResidence()
       );
    }

    @Override
    public boolean existsUserWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM "user"
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsUserWithId(Integer id) {
        var sql = """
                SELECT count(id)
                FROM "user"
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteUserById(Integer userId) {
        var sql = """
                SELECT count(id)
                FROM "user"
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, userId);
        System.out.println("deleteUserById result = " + result);
    }

    @Override
    public void updateUser(User update) {
       if(update.getFirst_name() != null) {
           String sql = "UPDATE user SET first_name = ? WHERE id = ? ";
           int result = jdbcTemplate.update(
                   sql,
                   update.getFirst_name(),
                   update.getId()
           );
           System.out.println("update user result = " + result);
       }
        if(update.getLast_name() != null) {
            String sql = "UPDATE user SET last_name = ? WHERE id = ? ";
            int result = jdbcTemplate.update(
                    sql,
                    update.getLast_name(),
                    update.getId()
            );
            System.out.println("update user result = " + result);
        }
        if(update.getPhone_number() != null) {
            String sql = "UPDATE user SET phone_number = ? WHERE id = ? ";
            int result = jdbcTemplate.update(
                    sql,
                    update.getPhone_number(),
                    update.getId()
            );
            System.out.println("update user result " + result);
        }
        if(update.getEmail() != null) {
            String sql = "UPDATE user SET email = ? WHERE id = ? ";
            int result = jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId()
            );
            System.out.println("update user result = " + result);
        }
        if(update.getResidence() != null) {
            String sql = "UPDATE user SET residence = ? WHERE id = ? ";
            int result = jdbcTemplate.update(
                    sql,
                    update.getResidence(),
                    update.getId()
            );
            System.out.println("update user result = " + result);
        }
    }
}
