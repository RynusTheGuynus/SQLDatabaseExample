package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{


    private JdbcUserDao sut;


    private static User TEST_USER_1;
    private static User TEST_USER_2;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);

        TEST_USER_1 = sut.create("TestUser1", "badpassword");
        TEST_USER_2 = sut.create("TestUser2", "weakpassword");

    }

    @Test
    public void createNewUser() {

        Assert.assertEquals("TestUser1", TEST_USER_1.getUsername());
    }

    @Test
    public void findIdByUsername_returns_correct_id_for_username() {

        User actual = sut.findById(sut.findIdByUsername("TestUser1"));
        Assert.assertEquals(TEST_USER_1.toString(), actual.toString());
    }

    @Test
    public void findAll_returns_a_list_of_users() {

        List<User> users = new ArrayList<>();

        users.add(TEST_USER_1);
        users.add(TEST_USER_2);

        Assert.assertEquals(users.size(), sut.findAll().size());
    }

    @Test
    public void findByUsername_returns_correct_id_for_username() {
        String actual = sut.findByUsername("TestUser1").getUsername();
        String expected = "TestUser1";


        Assert.assertEquals(expected, actual);

    }


    @Test
    public void findById() {
        int expected = 1001;
        User actual = sut.findByUsername("TestUser1");


        Assert.assertEquals(TEST_USER_1.toString(), actual.toString());
    }

    @Test
    public void getAllBalance() {
        BigDecimal expected = new BigDecimal("1000.00");
        BigDecimal actual = sut.getAllBalance(TEST_USER_1.getId());

        Assert.assertEquals(expected, actual);

    }


    @Test
    public void delete_successfully_deletes_user() {

        sut.deleteUser(1001);

        User expected = null;

        Assert.assertEquals(expected, sut.findById(1001));
    }




}
