package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    User findById(int id);

    User create(String username, String password);

    User update(User user);

    BigDecimal getAllBalance(int id);

    void deleteUser(int id);
}
