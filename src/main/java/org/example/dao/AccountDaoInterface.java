package org.example.dao;

import org.example.model.Account;

public interface AccountDaoInterface {

    Account addNewAccount(String name, Integer initialBalance);
    void makeTransaction(Account ac1, Account ac2, Integer quantity);

    Account getAccountById(Integer id);
}
