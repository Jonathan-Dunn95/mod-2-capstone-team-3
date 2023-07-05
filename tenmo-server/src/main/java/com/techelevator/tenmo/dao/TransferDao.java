package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> getTransfersByAccountId(int accountId);
    Transfer getTransfer(int transferId);
    void send(int senderId, int receiverId, BigDecimal transferAmount);
    boolean create(Transfer transfer);
}
