package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTest extends BaseDaoTests {

    private JdbcAccountDao accountDao;
    private JdbcUserDao userDao;
    private JdbcTemplate jdbcTemplate;

    private static User USER_1;
    private static User USER_2;

    private int expected;
    private int expected2;
    private int expected3;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        accountDao = new JdbcAccountDao(jdbcTemplate);
        userDao = new JdbcUserDao(jdbcTemplate);
        //Put dummy users into test DB
        USER_1 = userDao.create("User1", "badpassword");
        USER_2 = userDao.create("User2", "weakpassword");
        expected = USER_1.getId() + 1000;
        expected2 = USER_2.getId() + 1000;
        expected3 = -1;

    }

    @Test
    public void test_findAll() {
        int expected = 2;
        //Expect 2 accounts back, 1 for each user
        List<Account> actual = accountDao.findAll();
        Assert.assertEquals(expected, actual.size());
    }

    @Test
    public void test_findById() {
        //Account ID is always user + 1000
        Account actual1 = accountDao.findById(expected);
        Account actual2 = accountDao.findById(expected2);
        Account actual3 = accountDao.findById(expected3);
        Assert.assertEquals(expected, actual1.getAccountId());
        Assert.assertEquals(expected2, actual2.getAccountId());
        Assert.assertNotEquals(expected3, actual3);
    }

    @Test
    public void test_findByUsername() {
        int actual = accountDao.findByUsername("User1").getAccountId();
        int actual2 = accountDao.findByUsername("User2").getAccountId();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void test_findByUserId() {
        int actual = accountDao.findByUserId(USER_1.getId()).getAccountId();
        int actual2 = accountDao.findByUserId(USER_2.getId()).getAccountId();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void test_updateBalance() {
        BigDecimal expected = new BigDecimal("500.00");
        BigDecimal expected2 = new BigDecimal("0.00");

        BigDecimal actual = accountDao.updateBalance(accountDao.findByUserId(USER_1.getId()).getAccountId(), expected).getBalance();
        BigDecimal actual2 = accountDao.updateBalance(accountDao.findByUserId(USER_2.getId()).getAccountId(), expected2).getBalance();

        Assert.assertEquals(expected.doubleValue(), actual.doubleValue(), 0.001);
        Assert.assertEquals(expected2.doubleValue(), actual2.doubleValue(), 0.001);
    }

    @Test
    public void test_mapRowToAccount() {
        Account expected = new Account(
                USER_1.getId() + (1000),
                USER_1.getId(),
                new BigDecimal("1000.00")
        );
        Account actual = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM account WHERE account_id = ?;", expected.getAccountId());
        if (results.next()) {
            actual = accountDao.mapRowToAccount(results);
        }

        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getBalance().doubleValue(), actual.getBalance().doubleValue(), 0.001);
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
    }
}
