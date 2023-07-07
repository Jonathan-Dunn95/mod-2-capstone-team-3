package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
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

    @RequestMapping(value = "/tenmo_user", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

//    @RequestMapping(value = "/account/{account_id}", method = RequestMethod.GET)
//    public Account getAccountById(Principal principal){
//        return accountDao.getAccountById(accountId);
//    }

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceById(Principal principal){
        String userName = principal.getName();
        int id = userDao.findIdByUsername(userName);
        return accountDao.getAccountBalance(id);
    }

    @RequestMapping(value = "/transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfersByUser (Principal principal){
        String userName = principal.getName();
        int id = userDao.findIdByUsername(userName);
        return transferDao.getTransfersByUserId(id);
    }

    @RequestMapping(value = "/transfers/{transfer_id}", method = RequestMethod.GET)
    public Transfer getTransfersById(@PathVariable("transfer_id") int transferId){
        return transferDao.getTransfer(transferId);
    }

    // value = "/transfer?receiver_id="
    @RequestMapping(value = "/transfers", method = RequestMethod.PUT)
    public Transfer sendTransfer(Principal principal,
                                 @RequestBody Transfer transfer) {
        String userName = principal.getName();
        int id  = userDao.findIdByUsername(userName);
        return transferDao.send(id, transfer);
    }

}









