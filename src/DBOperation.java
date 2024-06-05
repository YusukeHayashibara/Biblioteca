//
import javax.swing.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBOperation {

    // Insert a row into the patron table
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

    // Read data from the patron table
    public void readPatronData(Connection conn) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM patron;";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getInt("id_patron") + " ");
                System.out.print(rs.getString("nome") + " ");
                System.out.print(rs.getString("phone") + " ");
                System.out.println(rs.getString("address"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Update the name of a patron
    public boolean updatePatronName(Connection conn, String oldName, String newName) {
        Statement statement;
        try {
            String query = String.format("UPDATE patron SET nome='%s' WHERE nome='%s';", newName, oldName);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Patron Name Updated");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    // Search for a patron by name
    public void searchPatronByName(Connection conn, String name) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("SELECT * FROM patron WHERE nome='%s';", name);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getInt("id_patron") + " ");
                System.out.print(rs.getString("nome") + " ");
                System.out.print(rs.getString("phone") + " ");
                System.out.println(rs.getString("address"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Search for a patron by ID
    public void searchPatronById(Connection conn, int id) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("SELECT * FROM patron WHERE id_patron=%d;", id);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getInt("id_patron") + " ");
                System.out.print(rs.getString("nome") + " ");
                System.out.print(rs.getString("phone") + " ");
                System.out.println(rs.getString("address"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void searchBookById(Connection conn, int ISBM, JTextArea bookTextArea) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            String query = String.format("SELECT * FROM book WHERE ISBM=%d;", ISBM);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);

            StringBuilder bookData = new StringBuilder();
            while (rs.next()) {
                // Append each column value to the StringBuilder
                bookData.append("Title: ").append(rs.getString("title")).append("\n");
                bookData.append("Author: ").append(rs.getString("author")).append("\n");
                bookData.append("Category: ").append(rs.getString("category")).append("\n");
                // Add more columns if needed
                bookData.append("\n");
            }

            // Set the StringBuilder content to the JTextArea
            bookTextArea.setText(bookData.toString());

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            // Close ResultSet and Statement in the finally block
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }


    // Delete a patron by name
    public boolean deletePatronByName(Connection conn, String name) {
        Statement statement;
        try {
            String query = String.format("DELETE FROM patron WHERE nome='%s';", name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Patron Row Deleted");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

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

    // Delete a patron by ID
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

    // Insert a row into the book table
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

    // Read data from the book table
    public void readBookData(Connection conn) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM book;";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getInt("ISBN") + " ");
                System.out.print(rs.getString("title") + " ");
                System.out.print(rs.getString("author") + " ");
                System.out.println(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Update the title of a book
    public boolean updateBookTitle(Connection conn, String oldTitle, String newTitle) {
        Statement statement;
        try {
            String query = String.format("UPDATE book SET title='%s' WHERE title='%s';", newTitle, oldTitle);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Book Title Updated");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    // Search for a book by title
    public void searchBookByTitle(Connection conn, String title) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("SELECT * FROM book WHERE title='%s';", title);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getInt("ISBN") + " ");
                System.out.print(rs.getString("title") + " ");
                System.out.print(rs.getString("author") + " ");
                System.out.println(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    // Delete a book by title
    public boolean deleteBookByTitle(Connection conn, String title) {
        Statement statement;
        try {
            String query = String.format("DELETE FROM book WHERE title='%s';", title);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Book Row Deleted");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    // Insert a row into the loan table
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

    // Read data from the loan table
    public void readLoanData(Connection conn) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM loan;";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getInt("ISBN") + " ");
                System.out.print(rs.getInt("id_patron") + " ");
                System.out.println(rs.getDate("loan_date"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Delete a loan by primary key (ISBN, id_patron, loan_date)
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

    // Other methods (update, search by id, etc.) would be similar and adapted for each table
}
