package com.revature.Java.Service;

import com.revature.Java.DAO.AccountDAO;
import com.revature.Java.Model.Account;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account registerAccount(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }

        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }

        Account registeredAccount = accountDAO.insertAccount(account);
        if (registeredAccount != null) {
            return registeredAccount;
        } else {
            return null;
        }
    }

    public Account login(String username, String password) {
        Account account = accountDAO.getAccountByUsernameAndPassword(username, password);
        if (account != null) {
            return account;
        }
        return null;
    }
}
