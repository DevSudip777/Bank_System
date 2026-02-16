package dao;

import model.Account;
import model.Transaction;
import util.DBUtil;
import java.sql.Timestamp;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public void addTransaction(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionDate, RelatedAccountNumber, Description) VALUES (?,?,?,?,?,?)";
        // create connection with database then prepare a statement.
        try(
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            // set all attributes------------
            ps.setLong(1, t.getAccountNumber());
            ps.setString(2, t.getTransactionType());
            ps.setDouble(3, t.getAmount());
            ps.setTimestamp(4, Timestamp.valueOf(t.getTransactionDate()));
            if (t.getRelatedAccountNumber() != 0) {
                ps.setLong(5, t.getRelatedAccountNumber());
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.setString(6, t.getDescription());
            // update the statement
            ps.executeUpdate();
        }
    }
    public void addTransferTransaction(Transaction t, Connection conn) throws  SQLException{
        String sql = "INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionDate, RelatedAccountNumber, Description) VALUES (?,?,?,?,?,?)";
        // create connection with database then prepare a statement.
        try(
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            // set all attributes------------
            ps.setLong(1, t.getAccountNumber());
            ps.setString(2, t.getTransactionType());
            ps.setDouble(3, t.getAmount());
            ps.setTimestamp(4, Timestamp.valueOf(t.getTransactionDate()));
            if (t.getRelatedAccountNumber() != 0) {
                ps.setLong(5, t.getRelatedAccountNumber());
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.setString(6, t.getDescription());
            // update the statement
            ps.executeUpdate();
        }
    }
    public List<Transaction> getTransactions(long accNumber) throws SQLException{
        // create a string url
        String sql = "SELECT * FROM transactions WHERE AccountNumber = ? ORDER BY TransactionDate DESC";

        // create a connection
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setLong(1, accNumber);
            // getting resultSet from SQL
            ResultSet record = ps.executeQuery();
            // create a transactionList
            List<Transaction> transactionList = new ArrayList<>();

            while (record.next()){
                Timestamp ts = record.getTimestamp("TransactionDate");
                LocalDateTime ldt = ts.toLocalDateTime();
                Transaction transaction = new Transaction(
                        record.getLong("AccountNumber"),
                        record.getString("TransactionType"),
                        record.getDouble("Amount"),
                        ldt,
                        record.getLong("RelatedAccountNumber"),
                        record.getString("Description")
                );
                transaction.setTransactionId(record.getInt("TransactionID"));
                transactionList.add(transaction);
            }
            return transactionList;
        }
    }
}
