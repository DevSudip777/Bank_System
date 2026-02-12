package dao;

import model.Account;
import model.Transaction;
import util.DBUtil;
import java.sql.Timestamp;

import java.sql.*;

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
    public void addTransferTransaction(Transaction t1, Connection conn){

    }
}
