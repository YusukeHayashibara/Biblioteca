import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class DBConnection is responsible for establishing a connection to a PostgreSQL database.
 */
public class DBConnection {

    /**
     * Establishes a connection to the specified PostgreSQL database.
     *
     * @param dbname the name of the database
     * @param user the username for the database
     * @param pass the password for the database
     * @return a Connection object if the connection is successful, null otherwise
     */
    public Connection connect_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
        return conn;
    }
}
