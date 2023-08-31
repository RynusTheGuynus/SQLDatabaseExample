package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@Component
@RestController
public class UserController {

    @Autowired
    private JdbcUserDao dao;

    private final String API_BASE_URL = "/user/";

    //Admins can get list of all users
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = API_BASE_URL, method = RequestMethod.GET)
    public List<User> getUsers() {
        return dao.findAll();
    }

    //Authenticated users can look up a user by ID
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = API_BASE_URL + "id/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int id) {
        return dao.findById(id);
    }

    //Authenticated users can look up a user by their username
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = API_BASE_URL + "{username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username) {
        return dao.findByUsername(username);
    }

    //Unauthenticated users can create a new user for themselves
    @RequestMapping(path = API_BASE_URL, method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        return dao.create(user.getUsername(), user.getPassword());
    }

    //The owner of the logged in account can update their account details
    @PreAuthorize("#username == authentication.principal.username")
    @RequestMapping(path = API_BASE_URL + "{username}", method = RequestMethod.PUT)
    public User updateUser(@PathVariable String username, @AuthenticationPrincipal Authentication authentication) {
        return dao.update(dao.findByUsername(username));
    }

    //The owner of the logged in account can get their balance from their account
    @PreAuthorize("#username == authentication.principal.username")
    @RequestMapping(path = API_BASE_URL + "{username}/account", method = RequestMethod.GET)
    public BigDecimal getAllBalance(@PathVariable String username, @AuthenticationPrincipal Authentication authentication){
        return dao.getAllBalance(dao.findIdByUsername(username));
    }

    //Admins can delete users
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = API_BASE_URL + "{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable int id) {
        dao.deleteUser(id);
    }

}



