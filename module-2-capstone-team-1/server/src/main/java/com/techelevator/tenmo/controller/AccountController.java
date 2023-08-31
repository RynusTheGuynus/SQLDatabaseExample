package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Component
@RestController
public class AccountController {

    @Autowired
    private JdbcAccountDao dao;

    private final String API_BASE_URL = "/account/";

    //Admins can retrieve a list of all accounts
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = API_BASE_URL, method = RequestMethod.GET)
    public List<Account> getAccounts() {
        return dao.findAll();
    }

    //Admins can retrieve an account by its ID
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = API_BASE_URL + "/{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable int id) {
        return dao.findById(id);

    }
}
