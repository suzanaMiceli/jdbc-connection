package org.example;

import org.example.connection.JdbcConnection;
import org.example.dao.AccountDao;
import org.example.dao.AccountDaoInterface;
import org.example.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Connection conn = JdbcConnection.getConnection();
        AccountDaoInterface accountDao = new AccountDao(conn);

        Account ac1 = accountDao.addNewAccount("Maycon", 100);
        Account ac2 = accountDao.addNewAccount("Felipe", 200);

        try {
            accountDao.makeTransaction(ac1, ac2, 50);
        } catch (Exception e) {
            log.info("Nao é possivel transferir o valor solicitado, saldo insuficiente");
        }


    }
}