package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private static final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(1000.00);

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        int id = -1;
        try {
            id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        try {

        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        try {

        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()) {
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE user_id = ?;";
        try {

        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return mapRowToUser(rowSet);
        }
        return null;
    }

    @Override
    public User create(String username, String password) {
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id;";
        String sql2 = "INSERT INTO account (user_id, balance) VALUES (?, ?) RETURNING account_id;";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        int newUserId = 0;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
            int newAccountId = jdbcTemplate.queryForObject(sql2, Integer.class, newUserId, DEFAULT_BALANCE);
        } catch (NullPointerException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return findById(newUserId);
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE tenmo_user SET password_hash = ? WHERE user_id = ?;";
        try {
            jdbcTemplate.update(sql, user.getPassword(), user.getId());
        } catch (NullPointerException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public BigDecimal getAllBalance(int id) {
        String sql = "SELECT balance FROM account JOIN tenmo_user USING (user_id) WHERE user_id = ?;";
        BigDecimal balance = null;
        try {
            balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
        } catch (NullPointerException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM tenmo_user WHERE user_id = ?;";
        String sql2 = "DELETE FROM account WHERE user_id =?;";
        try {
            jdbcTemplate.update(sql2, id);
            jdbcTemplate.update(sql, id);
        } catch (NullPointerException | DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
