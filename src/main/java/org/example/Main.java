package org.example;

import org.example.connection.JdbcConnection;
import org.example.dao.AccountDao;
import org.example.dao.AccountDaoInterface;
import org.example.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Connection conn = JdbcConnection.getConnection();
        AccountDaoInterface accountDao = new AccountDao(conn);

        Account ac1 = accountDao.addNewAccount("Maycon", 100);
        Account ac2 = accountDao.addNewAccount("Felipe", 200);

        log.info(String.format("maycon's account balance: %d", ac1.balance()));
        log.info(String.format("felipe's account balance: %d", ac2.balance()));

        try {
            accountDao.makeTransaction(ac1, ac2, 50);

            Account mayconAccount = accountDao.getAccountById(ac1.id());
            Account felipeAccount = accountDao.getAccountById(ac2.id());

            log.info(String.format("maycon's account balance: %d", mayconAccount.balance()));
            log.info(String.format("felipe's account balance: %d", felipeAccount.balance()));

        } catch (Exception e) {
            log.info("Nao é possivel transferir o valor solicitado, saldo insuficiente");
            log.error(e.getMessage());
        }


    }
}