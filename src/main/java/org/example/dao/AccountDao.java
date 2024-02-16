package org.example.dao;

import org.example.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AccountDao implements AccountDaoInterface{

    private final Connection conn;
    private static final Logger log = LoggerFactory.getLogger(AccountDao.class);

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
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try {
            conn.setAutoCommit(false);
            String decreaseQuantitySql = "update account set balance = balance - ? where id = ?";
            ps1 = conn.prepareStatement(decreaseQuantitySql);
            ps1.setInt(1, quantity);
            ps1.setInt(2, ac1.id());

            ps1.executeUpdate();
            log.info(String.format("decrease quantity of %s's account ", ac1.name()));

            String addQuantitySql = "update account set balance = balance + ? where id = ?";
            ps2 = conn.prepareStatement(addQuantitySql);
            ps2.setInt(1, quantity);
            ps2.setInt(2, ac2.id());

            ps2.executeUpdate();
            log.info(String.format("add quantity in %s's account", ac2.name()));

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(ps1);
            closeStatement(ps2);
        }
    }

    @Override
    public Account getAccountById(Integer id) {
        String sql = "select id, name, balance from account where id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
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
