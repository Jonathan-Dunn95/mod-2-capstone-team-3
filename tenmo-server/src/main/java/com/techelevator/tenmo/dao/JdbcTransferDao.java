package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
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
    public List<Transfer> getTransfersByUserId(int userId){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, sender_id, receiver_id, transfer_amount " +
                "FROM transfer WHERE sender_id = ? " +
                "OR receiver_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
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
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        if(result.next()){
            return mapRowToTransfer(result);
        } else {
            return null;
        }
    }

    @Override
    @ResponseStatus(HttpStatus.ACCEPTED)    //cant find approved status
    public Transfer send(int id, Transfer transfer) {

            String sql3 = "INSERT INTO transfer (sender_id, receiver_id, transfer_amount) " +
                    "VALUES (?, ?, ?) RETURNING transfer_id";
            int transferId = jdbcTemplate.queryForObject(sql3, Integer.class, id, transfer.getReceiverId(), transfer.getTransferAmount());
            Transfer newTransfer = getTransfer(transferId);

            String sql = "UPDATE account " +
                    "SET balance = (balance+?) " +
                    "WHERE account_id = (SELECT account_id FROM account WHERE user_id = ?) " +
                    "AND ? <= (SELECT balance FROM account WHERE user_id = ?)";
            jdbcTemplate.update(sql, newTransfer.getTransferAmount(), newTransfer.getReceiverId(), newTransfer.getTransferAmount(), id);

            String sql2 = "UPDATE account " +
                    "SET balance = (balance-?) " +
                    "WHERE account_id = (SELECT account_id FROM account WHERE user_id = ?)" +
                    "AND ? <= (SELECT balance FROM account WHERE user_id = ?)";
            jdbcTemplate.update(sql2, newTransfer.getTransferAmount(), id, newTransfer.getTransferAmount(), id);

            return newTransfer;
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
