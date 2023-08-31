package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTests{

    private JdbcTransferDao transferDao;
    private JdbcUserDao userDao;
    private JdbcTemplate jdbcTemplate;

    private static Transfer TRANSFER_1;
    private static Transfer TRANSFER_2;
    private static Transfer TRANSFER_3;
    private static User TEST_USER_1;
    private static User TEST_USER_2;
    private static User TEST_USER_3;

    private int expected;
    private int expected2;
    private int expected3;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        transferDao = new JdbcTransferDao(jdbcTemplate);
        userDao = new JdbcUserDao(jdbcTemplate);

        TEST_USER_1 = userDao.create("User1", "password");
        TEST_USER_2 = userDao.create("User2", "password");
        TEST_USER_3 = userDao.create("User3", "password");
        TRANSFER_1 = transferDao.create(TEST_USER_1.getId() + 1000, TEST_USER_2.getId() + 1000, new BigDecimal("200.00"), "PENDING", TEST_USER_1.getId(),"User1", "User2");
        TRANSFER_2 = transferDao.create(TEST_USER_1.getId() + 1000, TEST_USER_2.getId() + 1000, new BigDecimal("1.50"), "PENDING", TEST_USER_1.getId(),"User1", "User2");
        TRANSFER_3 = transferDao.create(TEST_USER_2.getId() + 1000, TEST_USER_2.getId() + 1000, new BigDecimal("-50.00"), "PENDING", TEST_USER_2.getId(),"User2", "User2");


    }

    @Test
    public void create_returns_new_transfer() {
        Transfer expected = TRANSFER_1;
        Transfer actual = transferDao.create(TRANSFER_1.getTransferFrom(), TRANSFER_1.getTransferTo(),
                TRANSFER_1.getTransferAmount(), TRANSFER_1.getTransferStatus(), TRANSFER_1.getUserId(),
                TRANSFER_1.getFromUsername(), TRANSFER_1.getToUsername());
        Assert.assertEquals(expected.getTransferFrom(), actual.getTransferFrom());
        Assert.assertEquals(expected.getTransferTo(), actual.getTransferTo());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getFromUsername(), actual.getFromUsername());
        Assert.assertEquals(expected.getToUsername(), actual.getToUsername());
    }

    @Test
    public void listTransfers_correctly_returns_correct_number_of_transfers(){
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(TRANSFER_1);
        transfers.add(TRANSFER_2);
        transfers.add(TRANSFER_3);
        Assert.assertEquals(transfers.size(), transferDao.listTransfers().size());
    }

    @Test
    public void getByTransferId_returns_correct_transfer_id() {
        int expected = TRANSFER_1.getTransferId();
        int expected2 = TRANSFER_2.getTransferId();
        int actual = transferDao.getByTransferId(expected).getTransferId();
        int actual2 = transferDao.getByTransferId(expected2).getTransferId();
        Assert.assertEquals(expected,actual);
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void getByUsername_returns_correct_list_of_transfers () {
        List<Transfer> expected = new ArrayList<>();
        expected.add(TRANSFER_1);
        expected.add(TRANSFER_2);

        List<Transfer> actual = transferDao.getByUsername("User1");
        Assert.assertEquals(expected.size(), actual.size());
    }

    @Test
    public void mapRowToTransfer() {
        Transfer expected = TRANSFER_1;
        Transfer actual = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM transfer WHERE transfer_id = ?;", expected.getTransferId());
        if (results.next()) {
            actual = transferDao.mapRowToTransfer(results);
        }
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());

    }
}

