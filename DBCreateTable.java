import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The DBCreateTable class provides a method to create necessary tables in the database if they do not already exist.
 */
public class DBCreateTable {

    /**
     * Creates the tables 'patron', 'book', and 'loan' in the database if they do not already exist.
     *
     * @param connection the database connection to use for creating the tables
     */
    public static void createTables(Connection connection) {
        String createPatronTable = "CREATE TABLE IF NOT EXISTS patron (" +
                "id_patron SERIAL PRIMARY KEY, " +
                "nome VARCHAR(100) NOT NULL, " +
                "phone INT NOT NULL, " +
                "address VARCHAR(100) NOT NULL" +
                ");";

        String createBookTable = "CREATE TABLE IF NOT EXISTS book (" +
                "id_book SERIAL PRIMARY KEY, " +
                "title VARCHAR(100) NOT NULL, " +
                "author VARCHAR(100) NOT NULL, " +
                "genre VARCHAR(100) NOT NULL" +
                ");";

        String createLoanTable = "CREATE TABLE IF NOT EXISTS loan (" +
                "id_book INT NOT NULL, " +
                "id_patron INT NOT NULL, " +
                "loan_date DATE NOT NULL, " +
                "return_date DATE, " +
                "Returned BOOLEAN, " +
                "CONSTRAINT loan_book_fk FOREIGN KEY (id_book) REFERENCES book(id_book) ON DELETE CASCADE, " +
                "CONSTRAINT loan_patron_fk FOREIGN KEY (id_patron) REFERENCES patron(id_patron) ON DELETE CASCADE, " +
                "CONSTRAINT pk_loan PRIMARY KEY (id_book, id_patron, loan_date)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPatronTable);
            stmt.execute(createBookTable);
            stmt.execute(createLoanTable);
            System.out.println("Tables created or already exist.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
