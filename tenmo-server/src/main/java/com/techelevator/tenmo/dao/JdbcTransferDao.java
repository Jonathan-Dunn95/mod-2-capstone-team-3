package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfersByAccountId(int accountId){       //add subqueries for user_ids
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, sender_id, receiver_id, transfer_amount " +
                "FROM transfer WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, Transfer.class, accountId);
        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int transferId){
        String sql = "SELECT transfer_id, sender_id, receiver_id, transfer_amount " +
                "FROM transfer WHERE transfer_id = ?";
        Transfer result = jdbcTemplate.queryForObject(sql, Transfer.class, transferId);
        return result;
    }

    @Override
    @ResponseStatus(HttpStatus.ACCEPTED)    //cant find approved status
    public void send(int senderId, int receiverId, BigDecimal transferAmount){
//        if (receiverId == senderId){
//            //cannot send to self id
//        }
        String sql = "UPDATE account SET balance = (balance+?) WHERE account_id = ?";// increase receiver balance
        jdbcTemplate.update(sql, transferAmount, receiverId);

        String sql2 = "UPDATE account SET balance = (balance-?) WHERE account_id = ?";        //decrease senders balance
        jdbcTemplate.update(sql2, transferAmount, senderId);
    }

    @Override
    public boolean create(Transfer transfer){
        String sql = "INSERT INTO transfer (sender_id, receiver_id, transfer_amount) " +
                "VALUES (?, ?, ?)";
        try{
           jdbcTemplate.update(sql, transfer.getSenderId(), transfer.getReceiverId(), transfer.getTransferAmount());
        }catch (DataAccessException dae){
            return false;
        }
        return true;
    }

    Transfer mapRowToTransfer(SqlRowSet sq){
        Transfer transfer = new Transfer();
        transfer.setReceiverId(sq.getInt("receiver_id"));
        transfer.setSenderId(sq.getInt("sender_id"));
        transfer.setTransferAmount(sq.getBigDecimal("transfer_amount"));
        transfer.setTransferId(sq.getInt("transfer_id"));
        return transfer;
    }

}
