package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    List<Transfer> listTransfers();

    Transfer getByTransferId(int transferId);

    List<Transfer> getByUsername(String username);

    Transfer create(int from, int to, BigDecimal amount, String status, int userId, String fromUsername, String toUsername);

    Transfer mapRowToTransfer(SqlRowSet rowSet);
}
