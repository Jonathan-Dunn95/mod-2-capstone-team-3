package com.techelevator.tenmo.model;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

public class Account implements Serializable {

    @Positive
    private int accountId;
    private int userId;
    private BigDecimal balance;

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public Account() {}

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
