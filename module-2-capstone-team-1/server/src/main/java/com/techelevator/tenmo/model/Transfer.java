package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private BigDecimal transferAmount;
    private String transferStatus;
    private int transferFrom;
    private int transferTo;
    private int userId;
    private String fromUsername;
    private String toUsername;

    public Transfer(int transferId, BigDecimal transferAmount, String transferStatus, int transferFrom, int transferTo, int userId, String fromUsername, String toUsername) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.transferStatus = transferStatus;
        this.transferFrom = transferFrom;
        this.transferTo = transferTo;
        this.userId = userId;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }

    public Transfer(int transferId, BigDecimal transferAmount, int transferFrom, int transferTo, int userId, String fromUsername, String toUsername) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.transferStatus = "PENDING";
        this.transferFrom = transferFrom;
        this.transferTo = transferTo;
        this.userId = userId;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }

    public Transfer() {

    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(int transferFrom) {
        this.transferFrom = transferFrom;
    }

    public int getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(int transferTo) {
        this.transferTo = transferTo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }
}
