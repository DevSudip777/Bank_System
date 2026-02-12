package dao;

import model.Account;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDate;

public class AccountDAO {
    // create instance of account and store it in bankAccounts schema
    public boolean createAccount(Account acc) throws SQLException {
        String sql = "INSERT INTO bankaccounts (AccountNumber, CustomerID, AccountType, Balance, Status, OpeningDate) VALUES (?,?,?,?,?,?)";
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setLong(1, acc.getAccountNumber());
            ps.setInt(2, acc.getCustomerId());
            ps.setString(3, acc.getAccountType());
            ps.setDouble(4, acc.getBalance());
            ps.setString(5, acc.getStatus());
            ps.setDate(6, Date.valueOf(LocalDate.now()));

            int updatedRows = ps.executeUpdate();
            if(updatedRows == 0) return false;
        }
        return true;
    }

    // getting account from database
    public Account getAccount( long accNumber) throws SQLException{
        // sql query preparation
        String sql = "SELECT * FROM bankaccounts WHERE AccountNumber = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            // create connection. prepare the sql statement for execution.
            ps.setLong(1, accNumber);
            // execute query
            ResultSet record = ps.executeQuery();

            // extract details from the resultSet and create an object of Account class
            record.next();
            return new Account(
                    record.getLong("AccountNumber"),
                    record.getInt("CustomerID"),
                    record.getString("AccountType"),
                    record.getDouble("Balance"),
                    record.getString("Status"),
                    record.getDate("OpeningDate").toLocalDate());
        }

    }
    // close the bank account in database
    public boolean closeAccount(long accNumber) throws SQLException {
        String sql = "UPDATE bankaccounts SET Status = 'Closed' WHERE AccountNumber = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            // create connection. prepare the sql statement for execution.
            ps.setLong(1, accNumber);
            // execute query
            int rowsEffected = ps.executeUpdate();
            return  rowsEffected > 0;
        }
    }
    // Update the balance for withdrawal and deposit

    public void updateBalance(Account account) throws SQLException {
        String sql = "UPDATE bankaccounts SET Balance = ? WHERE AccountNumber = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, account.getBalance());
            ps.setLong(2, account.getAccountNumber());

            ps.executeUpdate();

        }
    }

    // Update the balance for transaction

    public boolean transactionUpdateBalance(Account account, Connection conn) throws SQLException{
        String sql = "UPDATE bankaccounts SET Balance = ? WHERE AccountNumber = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setDouble(1, account.getBalance());
            ps.setLong(2, account.getAccountNumber());

            ps.executeUpdate();
            return ps.executeUpdate() > 0;
        }
    }

}
