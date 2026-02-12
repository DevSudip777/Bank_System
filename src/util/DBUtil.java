package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String url = "jdbc:mysql://localhost:3306/bankmanagement";
    private static final String password = "samanta";
    private static final String user = "root";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
