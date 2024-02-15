package org.example.dao;

import org.example.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AccountDao implements AccountDaoInterface{

    private Connection conn;
    private static Logger log = LoggerFactory.getLogger(AccountDao.class);

    public AccountDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Account addNewAccount(String name, Integer initialBalance) {
        PreparedStatement ps = null;
        try{
            String sql = "insert into account (name, balance) values (?, ?)";

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, initialBalance);

            int rows = ps.executeUpdate();

            if(rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();

                if(rs.next()) {
                    return new Account(rs.getInt(1), name, initialBalance);
                }

                closeResultSet(rs);
            } else {
                log.error("unexpected error, no rows affected");
            }

        } catch (SQLException e) {
            log.error("error trying to insert in account table");
            throw new RuntimeException(e);
        }  finally {
            closeStatement(ps);
        }

        return null;
    }

    @Override
    public void makeTransaction(Account ac1, Account ac2, Integer quantity) {

    }

    @Override
    public Account getAccountById(Integer id) {
        String sql = "select id, name, balance from account where id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                return new Account(rs.getInt("id"), rs.getString("name"), rs.getInt("balance"));
            }
            throw new RuntimeException("account not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            closeStatement(ps);
            closeResultSet(rs);
        }

    }


    private void closeStatement(Statement ps) {
        if(ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                log.error("error closing prepared statement");
                throw new RuntimeException(e);
            }
        }
    }
    private void closeResultSet(ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("error closing result set");
                throw new RuntimeException(e);
            }
        }
    }
}
