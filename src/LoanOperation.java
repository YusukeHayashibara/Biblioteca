import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class LoanOperation provides methods to interact with the loan records in a database.
 */
public class LoanOperation {

    private Connection connection;
    /**
     * Constructs a LoanOperation with a specified database connection.
     *
     * @param connection the database connection to be used
     */
    public LoanOperation(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all loan records from the database.
     *
     * @return a ResultSet containing all loan records
     * @throws SQLException if a database access error occurs
     */
    public ResultSet getAllLoans() throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("SELECT * FROM loan");
    }

    /**
     * Deletes a specific loan record from the database.
     *
     * @param bookId the ID of the book
     * @param patronId the ID of the patron
     * @param loanDate the date the loan was made
     * @throws SQLException if a database access error occurs
     */
    public void deleteLoan(int bookId, int patronId, Date loanDate) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = String.format("DELETE FROM loan WHERE id_book=%d AND id_patron=%d AND loan_date='%s'", bookId, patronId, loanDate);
        stmt.executeUpdate(query);
    }

    /**
     * Updates a specific loan record in the database with new details.
     *
     * @param currentBookId the current ID of the book
     * @param currentPatronId the current ID of the patron
     * @param currentLoanDate the current date of the loan
     * @param newBookId the new ID of the book
     * @param newPatronId the new ID of the patron
     * @param newLoanDate the new date of the loan
     * @param newReturnDate the new return date of the loan
     * @param newReturned the new returned status of the loan
     * @throws SQLException if a database access error occurs
     */
    public void updateLoan(int currentBookId, int currentPatronId, Date currentLoanDate, int newBookId, int newPatronId, Date newLoanDate, Date newReturnDate, boolean newReturned) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = String.format("UPDATE loan SET id_book=%d, id_patron=%d, loan_date='%s', return_date='%s', returned=%b WHERE id_book=%d AND id_patron=%d AND loan_date='%s'",
                newBookId, newPatronId, newLoanDate, newReturnDate, newReturned, currentBookId, currentPatronId, currentLoanDate);
        stmt.executeUpdate(query);
    }

    /**
     * Adds a new loan record to the database.
     *
     * @param bookId the ID of the book
     * @param patronId the ID of the patron
     * @param loanDate the date of the loan
     * @param returnDate the return date of the loan (can be null)
     * @param returned the returned status of the loan
     * @throws SQLException if a database access error occurs
     */
    public void addLoan(int bookId, int patronId, Date loanDate, Date returnDate, boolean returned) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = String.format("INSERT INTO loan (id_book, id_patron, loan_date, return_date, returned) VALUES (%d, %d, '%s', %s, %b)",
                bookId, patronId, loanDate, returnDate == null ? "NULL" : "'" + returnDate + "'", returned);
        stmt.executeUpdate(query);
    }

    /**
     * Searches loan records in the database based on a query string.
     *
     * @param query the search query
     * @return a ResultSet containing the search results
     * @throws SQLException if a database access error occurs
     */
    public ResultSet searchLoan(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM loan WHERE CAST(id_book AS TEXT) ILIKE '%" + query + "%'";
        return stmt.executeQuery(sql);
    }
}
