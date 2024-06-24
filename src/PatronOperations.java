import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The PatronOperations class provides methods to perform CRUD (Create, Read, Update, Delete)
 * operations on a 'patron' table in a database.
 */
public class PatronOperations {

    private Connection connection;

    /**
     * Constructs a PatronOperations with a specified database connection.
     *
     * @param connection the database connection to be used for operations
     */
    public PatronOperations(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all patrons from the database.
     *
     * @return a ResultSet containing all patrons
     * @throws SQLException if a database access error occurs
     */
    public ResultSet getAllPatrons() throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("SELECT * FROM patron");
    }

    /**
     * Deletes a patron from the database based on the specified patron ID.
     *
     * @param patronId the ID of the patron to be deleted
     * @throws SQLException if a database access error occurs
     */
    public void deletePatron(int patronId) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = String.format("DELETE FROM patron WHERE id_patron=%d", patronId);
        stmt.executeUpdate(query);
    }

    /**
     * Updates a patron in the database with the specified details.
     *
     * @param patronId the ID of the patron to be updated
     * @param name the new name of the patron
     * @param phone the new phone number of the patron
     * @param address the new address of the patron
     * @throws SQLException if a database access error occurs
     */
    public void updatePatron(int patronId, String name, int phone, String address) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // SQL command to update patron information
            String query = String.format("UPDATE patron SET nome='%s', phone=%d, address='%s' WHERE id_patron=%d",
                    name.replace("'", "''"), // Prevent SQL injection
                    phone,
                    address.replace("'", "''"),
                    patronId);

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            // Handle SQL exceptions
            throw new SQLException("Error updating patron: " + e.getMessage());
        }
    }


    /**
     * Adds a new patron to the database with the specified details.
     *
     * @param name the name of the new patron
     * @param phone the phone number of the new patron
     * @param address the address of the new patron
     * @throws SQLException if a database access error occurs
     */
    public void addPatron(String name, int phone, String address) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // SQL command to insert patron into the database
            String query = String.format("INSERT INTO patron (nome, phone, address) VALUES ('%s', %d, '%s')",
                    name.replace("'", "''"), // Prevent SQL injection
                    phone,
                    address.replace("'", "''"));

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            // Handle SQL exceptions
            throw new SQLException("Error adding patron: " + e.getMessage());
        }
    }

    /**
     * Searches for patrons in the database that match the specified query.
     *
     * @param query the search query to find matching patrons
     * @return a ResultSet containing the search results
     * @throws SQLException if a database access error occurs
     */
    public ResultSet searchPatron(String query, String criteria) throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "";
        switch (criteria.toLowerCase()) {
            case "name":
                sql = "SELECT * FROM patron WHERE nome ILIKE '%" + query + "%'";
                break;
            case "phone":
                sql = "SELECT * FROM patron WHERE phone ILIKE '%" + query + "%'";
                break;
            case "address":
                sql = "SELECT * FROM patron WHERE address ILIKE '%" + query + "%'";
                break;
            case "id":
                sql = "SELECT * FROM patron WHERE id_patron ILIKE '%" + query + "%'";
                break;
            default:
                throw new IllegalArgumentException("Invalid search criteria.");
        }
        return stmt.executeQuery(sql);
    }
}
