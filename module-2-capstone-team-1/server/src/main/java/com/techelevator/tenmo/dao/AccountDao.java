package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> findAll();
    Account findById(int id);
    Account findByUsername(String username);
    Account findByUserId(int id);
    //Account create(Account account);
    //void delete(int id);

    Account updateBalance(int id, BigDecimal balance);

    Account mapRowToAccount(SqlRowSet rowSet);
    BigDecimal getBalance(int id);


}
