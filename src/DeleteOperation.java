import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteOperation {
    public boolean deleteBookById(Connection conn, int id) {
        Statement statement;
        try {
            String query = String.format("DELETE FROM book WHERE ISBN=%d;", id);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Patron Row Deleted");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    public boolean deletePatronById(Connection conn, int id) {
        Statement statement;
        try {
            String query = String.format("DELETE FROM patron WHERE id_patron=%d;", id);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Patron Row Deleted");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    public boolean deleteLoan(Connection conn, int ISBN, int idPatron, String loanDate) {
        Statement statement;
        try {
            String query = String.format("DELETE FROM loan WHERE ISBN=%d AND id_patron=%d AND loan_date='%s';", ISBN, idPatron, loanDate);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Loan Row Deleted");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}
