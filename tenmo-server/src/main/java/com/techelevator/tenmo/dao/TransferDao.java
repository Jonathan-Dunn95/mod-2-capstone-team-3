package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> getTransfersByUserId(int userId);
    Transfer getTransfer(int transferId);
    boolean send(int senderId, int receiverId, BigDecimal transferAmount);
}
