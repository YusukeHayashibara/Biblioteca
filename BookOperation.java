import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * The BookOperation class provides methods to perform CRUD (Create, Read, Update, Delete) 
 * operations on a 'book' table in a database.
 */
public class BookOperation {
    private Connection connection;

    /**
     * Constructs a BookOperation with a specified database connection.
     *
     * @param connection the database connection to be used for operations
     */
    public BookOperation(Connection connection) {
        this.connection = connection;
    }

    /**
     * Loads book data from the database into a specified DefaultTableModel.
     *
     * @param tableModel the DefaultTableModel to which book data will be added
     */
    public void loadBookData(DefaultTableModel tableModel) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM book")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id_book"), rs.getString("title"), rs.getString("author"), rs.getString("genre")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a book from the database based on the specified book ID.
     *
     * @param bookId the ID of the book to be deleted
     */
    public void deleteBook(int bookId) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = String.format("DELETE FROM book WHERE id_book=%d", bookId);
        stmt.executeUpdate(query);
    }


    /**
     * Updates a book in the database with the specified details.
     *
     * @param bookId the ID of the book to be updated
     * @param title the new title of the book
     * @param author the new author of the book
     * @param genre the new genre of the book
     */
    public void updateBook(int bookId, String title, String author, String genre) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Validate inputs before executing SQL query
            if (title == null || author == null || genre == null) {
                throw new IllegalArgumentException("Book title, author, or genre cannot be null.");
            }

            // SQL command to update book information
            String query = String.format("UPDATE book SET title='%s', author='%s', genre='%s' WHERE id_book=%d",
                    title.replace("'", "''"), // Prevent SQL injection
                    author.replace("'", "''"),
                    genre.replace("'", "''"),
                    bookId);

            int rowsUpdated = stmt.executeUpdate(query);
            if (rowsUpdated == 0) {
                throw new SQLException("No book found with id_book=" + bookId);
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            throw new SQLException("Error updating book: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Handle validation errors (e.g., null values)
            throw new IllegalArgumentException("Error updating book: " + e.getMessage());
        }
    }


    /**
     * Adds a new book to the database with the specified details.
     *
     * @param title the title of the new book
     * @param author the author of the new book
     * @param genre the genre of the new book
     */
    public void addBook(String title, String author, String genre) {
        try (Statement stmt = connection.createStatement()) {
            // Validate inputs before executing SQL query
            if (title == null || author == null || genre == null) {
                throw new IllegalArgumentException("Book title, author, or genre cannot be null.");
            }

            // SQL query to insert new book into the database
            String query = String.format("INSERT INTO book (title, author, genre) VALUES ('%s', '%s', '%s')",
                    title.replace("'", "''"), // Prevent SQL injection
                    author.replace("'", "''"),
                    genre.replace("'", "''"));

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while adding book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            // Handle validation errors (e.g., null values)
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Searches for books in the database that match the specified query and loads the results 
     * into a specified DefaultTableModel.
     *
     * @param query the search query to find matching books
     * @param tableModel the DefaultTableModel to which the search results will be added
     */
    public ResultSet searchBook(String query, int criteria) throws SQLException {
        String sql = "";
        switch (criteria) {
            case 1:
                sql = "SELECT * FROM book WHERE author ILIKE '%" + query + "%'";
                break;
            case 2:
                sql = "SELECT * FROM book WHERE id_book = " + query;
                break;
            case 3:
                sql = "SELECT * FROM book WHERE genre ILIKE '%" + query + "%'";
                break;
            case 4:
                sql = "SELECT * FROM book WHERE title ILIKE '%" + query + "%'";
                break;
            default:
                throw new IllegalArgumentException("Invalid search criteria");
        }

        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }
}
