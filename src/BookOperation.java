import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public void deleteBook(int bookId) throws SQLException{
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
    public void updateBook(int bookId, String title, String author, String genre) {
        try (Statement stmt = connection.createStatement()) {
            //SQL command to update book information
            String query = String.format("UPDATE book SET title='%s', author='%s', genre='%s' WHERE id_book=%d", title, author, genre, bookId);
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
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
            //sql to insert new books on the table
            String query = String.format("INSERT INTO book (title, author, genre) VALUES ('%s', '%s', '%s')", title, author, genre);
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for books in the database that match the specified query and loads the results 
     * into a specified DefaultTableModel.
     *
     * @param query the search query to find matching books
     * @param tableModel the DefaultTableModel to which the search results will be added
     */
    public void searchBook(String query, DefaultTableModel tableModel) {
        //searching for book, in the search bar the user is able to search by book id, title, author and genre
        String sql = "SELECT * FROM book WHERE title ILIKE '%" + query + "%' OR author ILIKE '%" + query + "%' OR genre ILIKE '%" + query + "%'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id_book"), rs.getString("title"), rs.getString("author"), rs.getString("genre")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
