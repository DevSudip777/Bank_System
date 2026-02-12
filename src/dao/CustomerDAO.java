package dao;
import model.Customer;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CustomerDAO {
    public int createCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers(FirstName, LastName, Email, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?)";
        // try with resource block
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        )
        {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());
//            ps.setString(6, LocalDateTime.now().toString());

            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                return -1;
            }
            // store the response
            ResultSet keys = ps.getGeneratedKeys();
            // process the response as per your requirement.
            if (keys.next()) {
                return keys.getInt(1);
            }

            return -1;
        }
    }
}
