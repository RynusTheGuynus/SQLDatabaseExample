package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM account;";
        List<Account> accountList = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Account account = mapRowToAccount(results);
                accountList.add(account);
            }
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return accountList;
    }

    @Override
    public Account findById(int id) {
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        Account account = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public Account findByUsername(String username) {
        String sql = "SELECT * FROM account JOIN tenmo_user USING (user_id) WHERE username = ?;";
        Account account = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public Account findByUserId(int id) {
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        Account account = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public Account updateBalance(int id, BigDecimal balance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(sql, balance, id);
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return findById(id);
    }

    @Override
    public Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setUserId(rowSet.getInt("user_id"));
        return account;
    }

    @Override
    public BigDecimal getBalance(int id) {
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                return results.getBigDecimal("balance");
            }
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


}
