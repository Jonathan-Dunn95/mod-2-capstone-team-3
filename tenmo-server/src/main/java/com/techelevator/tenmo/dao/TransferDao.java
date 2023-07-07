package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfersByUserId(int userId);

    Transfer getTransfer(int transferId);

    Transfer send(int id, Transfer transfer);
}
