package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcAccountDao jdbcAccountDao;

    @Autowired
    private JdbcUserDao jdbcUserDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcUserDao = new JdbcUserDao(jdbcTemplate);
    }

    @Override
    public Transfer create(int from, int to, BigDecimal amount, String status, int userId, String fromUsername, String toUsername) {
        int id = 0;
        Transfer transfer = null;
        String sql = "INSERT INTO transfer (transfer_amount, transfer_status, account_transfer_from, account_transfer_to, user_id, from_username, to_username)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            id = jdbcTemplate.queryForObject(sql, int.class, amount, status, from, to, userId, fromUsername, toUsername);
            transfer = getByTransferId(id);
            transfer.setTransferStatus(status);
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }


    @Override
    public List<Transfer> listTransfers() {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transferList.add(transfer);
            }
            return transferList;
        } catch (NullPointerException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Transfer getByTransferId(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                return transfer;
            }
        } catch (DataAccessException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Transfer> getByUsername(String username) {
        String sql = "SELECT * FROM transfer WHERE user_id = ?;";
        List<Transfer> transfers = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, jdbcUserDao.findByUsername(username).getId());
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (NullPointerException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfers;
    }

    @Override
    public Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferAmount(results.getBigDecimal("transfer_amount"));
        transfer.setTransferFrom(results.getInt("account_transfer_from"));
        transfer.setTransferTo(results.getInt("account_transfer_to"));
        transfer.setTransferStatus(results.getString("transfer_status"));
        transfer.setUserId(results.getInt("user_id"));
        transfer.setFromUsername(results.getString("from_username"));
        transfer.setToUsername(results.getString("to_username"));
        return transfer;
    }
}
