package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@Component
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private JdbcTransferDao jdbcTransferDao;
    @Autowired
    private JdbcAccountDao jdbcAccountDao;

    private final String API_BASE_URL = "/transfer/";

    //Authenticated users can create a pending transfer
    //A transfer from their own account is a send
    //A transfer from another account to their own is a request
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = API_BASE_URL, method = RequestMethod.POST)
    public boolean create(@RequestBody @NotNull Transfer transfer) {

        Account fromAccount = jdbcAccountDao.findById(transfer.getTransferFrom());

        if(transfer.getTransferFrom() == transfer.getTransferTo()
        || fromAccount.getBalance().doubleValue() <= 0
        || fromAccount.getBalance().doubleValue() <= transfer.getTransferAmount().doubleValue()
        || transfer.getTransferAmount().doubleValue() <= 0){
            transfer.setTransferStatus("DECLINED");
        } else{
            transfer.setTransferStatus("APPROVED");
        }

        //If transfer is not declined...
        if(!transfer.getTransferStatus().equals("DECLINED")){
            processBalance(transfer);
            jdbcTransferDao.create(transfer.getTransferFrom(), transfer.getTransferTo(), transfer.getTransferAmount(),
                    transfer.getTransferStatus(), transfer.getUserId(), transfer.getFromUsername(), transfer.getToUsername());
            return true;
        }
        else{
            jdbcTransferDao.create(transfer.getTransferFrom(), transfer.getTransferTo(), transfer.getTransferAmount(),
                    transfer.getTransferStatus(), transfer.getUserId(), transfer.getFromUsername(), transfer.getToUsername());
            throw new RuntimeException("Transfer was declined.");
        }
    }

    //Users can see a list of their own transfers
    @PreAuthorize("#username == authentication.principal.username")
    @RequestMapping(path = API_BASE_URL + "{username}", method = RequestMethod.GET)
    public List<Transfer> listByUsername(@PathVariable String username, @AuthenticationPrincipal Authentication authentication) {
        return jdbcTransferDao.getByUsername(username);
    }

    //Admins can retrieve a transfer by its ID
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = API_BASE_URL + "id/{id}", method = RequestMethod.GET)
    public Transfer getByTransferId(@PathVariable int id) {
        return jdbcTransferDao.getByTransferId(id);
    }

    //Admins can retrieve a list of all transfers
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = API_BASE_URL, method = RequestMethod.GET)
    public List<Transfer> list() {
        return jdbcTransferDao.listTransfers();
    }

    public Transfer processBalance(Transfer transfer){
        //Update FROM
        BigDecimal subtractedBalance = (jdbcAccountDao.getBalance(transfer.getTransferFrom()));
        subtractedBalance = subtractedBalance.subtract(transfer.getTransferAmount());
        jdbcAccountDao.updateBalance(transfer.getTransferFrom(), subtractedBalance);

        //Update TO
        BigDecimal addedBalance = (jdbcAccountDao.getBalance(transfer.getTransferTo()));
        addedBalance = addedBalance.add(transfer.getTransferAmount());
        jdbcAccountDao.updateBalance(transfer.getTransferTo(), addedBalance);
        return transfer;
    }

}
