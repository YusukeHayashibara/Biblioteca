import javax.swing.*;
import java.sql.Connection;

/**
 * The Main class is the entry point of the application. It establishes a connection to the database,
 * creates necessary tables and triggers if they do not exist, and launches the LibraryApp.
 */
public class Main {
    /**
     * The main method that serves as the entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();
        //Set your posgres password down below...
        Connection conn = dbConnection.connect_to_db("Biblioteca", "postgres", "YourPass");

        if (conn != null) {
            DBCreateTable.createTables(conn); // Create tables if they don't exist
            DBTriggers.createDeleteBookTrigger(conn);
            DBTriggers.createDeletePatronTrigger(conn);
            DBTriggers.createTriggers(conn);
            //Create New rows of books and patrons as an example, so if you wanna create yourself a complete new table
            //just delete following row.
            //Obs: Certify to remove this line after testing once, because for each run on main class you will be
            //adding the same books information and patron information and repeating on database
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