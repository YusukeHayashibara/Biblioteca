//
import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();
        Connection conn = dbConnection.connect_to_db("Biblioteca", "postgres", "1gatinho");

        if (conn != null) {
            DBCreateTable.createTables(conn); // Create tables if they don't exist
            //DBTriggers.createTriggers(conn);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LibraryApp(conn).setVisible(true);
                }
            });
        } else {
            System.err.println("Failed to connect to the database.");
        }
    }
}
