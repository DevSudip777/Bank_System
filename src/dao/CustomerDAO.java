package dao;
import exception.AccountNotFoundException;
import model.Customer;
import util.DBUtil;

import java.sql.*;

public class CustomerDAO {
    private final AccountDAO accountDAO = new AccountDAO();
    public int createCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers(FirstName, LastName, Email, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?)";
        // try with resource block
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        )
        {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());

            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                return -1;
            }
            // store the response
            // gives all auto generated keys from SQL database
            ResultSet keys = ps.getGeneratedKeys();
            // process the response as per your requirement.
            if (keys.next()) {
                // it gives key that is generated
                return keys.getInt(1);
            }
            return -1;
        }
    }
    // getting full name from database
    public String getCustomerName(long accountNumber) throws SQLException, AccountNotFoundException {

        String sql = "SELECT FirstName, LastName FROM customers Where CustomerID = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql))
        {
            int customerId = accountDAO.getAccount(accountNumber).getCustomerId();
            ps.setInt(1, customerId);

            // execute query
            ResultSet record = ps.executeQuery();
            boolean flag = record.next();
            if(!flag){
                throw new AccountNotFoundException("Customer does not exist at SS_Sev bank");
            }

            return record.getString("FirstName") +" "+ record.getString("LastName");
        }
    }

    // update the customer details to the SQL database
    public boolean updateFirstName(String fName, int customerID) throws SQLException{
        String sql = "UPDATE customers SET FirstName = ? WHERE CustomerID = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, fName);

            ps.setInt(2, customerID);

            // execute the query
            return  ps.executeUpdate() > 0;
        }
    }
    public boolean updateLastName(String lName, int customerID) throws SQLException{
        String sql = "UPDATE customers SET LastName = ? WHERE CustomerID = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, lName);
            ps.setInt(2, customerID);

            // execute the query
            return  ps.executeUpdate() > 0;
        }
    }
    public boolean updateEmail(String email, int customerID) throws SQLException{
        String sql = "UPDATE customers SET Email = ? WHERE CustomerID = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, email);

            ps.setInt(2, customerID);

            // execute the query
            return  ps.executeUpdate() > 0;
        }
    }
    public boolean updatePhone(String phone, int customerID) throws SQLException{
        String sql = "UPDATE customers SET PhoneNumber = ? WHERE CustomerID = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, phone);

            ps.setInt(2, customerID);

            // execute the query
            return  ps.executeUpdate() > 0;
        }
    }
    public boolean updateAddress(String address, int customerID) throws SQLException{
        String sql = "UPDATE customers SET Address = ? WHERE CustomerID = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, address);

            ps.setInt(2, customerID);

            // execute the query
            return  ps.executeUpdate() > 0;
        }
    }
}
