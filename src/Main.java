import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();
        Connection conn = dbConnection.connect_to_db("Biblioteca", "postgres", "1gatinho");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI(conn).setVisible(true);
            }
        });
    }
}
