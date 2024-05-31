import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertOperation {
    public boolean insertPatron(Connection conn, String name, String phone, String address) {
        Statement statement;
        try {
            String query = String.format("INSERT INTO patron(nome, phone, address) VALUES('%s', '%s', '%s');", name, phone, address);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Patron Row Inserted");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    public boolean insertBook(Connection conn, String title, String author, String category) {
        Statement statement;
        try {
            String query = String.format("INSERT INTO book(title, author, category) VALUES('%s', '%s', '%s');", title, author, category);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Book Row Inserted");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    public boolean insertLoan(Connection conn, int ISBN, int idPatron, String loanDate) {
        Statement statement;
        try {
            String query = String.format("INSERT INTO loan(ISBN, id_patron, loan_date) VALUES(%d, %d, '%s');", ISBN, idPatron, loanDate);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Loan Row Inserted");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}
