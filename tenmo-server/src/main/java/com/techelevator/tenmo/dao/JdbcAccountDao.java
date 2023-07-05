package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbctemplate) {
        this.jdbcTemplate = jdbctemplate;
    }

    @Override
    public BigDecimal getAccountBalance(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        return balance;
    }

    @Override
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        Account account = jdbcTemplate.queryForObject(sql, Account.class, accountId);
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id FROM account";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;

    }

    private Account mapRowToAccount(SqlRowSet qw) {
        Account account = new Account();
        account.setBalance(qw.getBigDecimal("balance"));
        account.setUserId(qw.getInt("user_id"));
        return account;
    }
}
