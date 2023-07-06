package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class AccountController {
    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public AccountController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public List<Account> listAllAccounts (){
        return accountDao.getAllAccounts();
    }

    @RequestMapping(value = "/account/{account_id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable("account_id") int accountId){
        return accountDao.getAccountById(accountId);
    }

    @RequestMapping(value = "account/{account_id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceById(@PathVariable("account_id") int accountId){
        return accountDao.getAccountBalance(accountId);
    }

    @RequestMapping(value = "/transfer/{account_id}", method = RequestMethod.GET)
    public List<Transfer> listTransfers (@PathVariable("account_id") int accountId){
        return transferDao.getTransfersByAccountId(accountId);
    }

    @RequestMapping(value = "/transfer/{transfer_id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable("transfer_id") int transferId){
        return transferDao.getTransfer(transferId);
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.PUT)
    public boolean sendTransferAmount(@RequestParam("sender_id") int senderId,
                                      @RequestParam("receiver_id") int receiverId,
                                      @RequestParam("transfer_amount") BigDecimal transferAmount) {
        return transferDao.send(senderId, receiverId, transferAmount);
    }

}









