import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class DBTriggers provides methods to create database triggers for the loan table.
 */
public class DBTriggers {

    /**
     * Creates triggers in the database to check book availability before inserting a new loan record.
     *
     * @param connection the database connection to be used
     */
    public static void createDeleteBookTrigger(Connection connection) {
        String createTriggerFunction = "CREATE OR REPLACE FUNCTION check_book_returned_before_deletion() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    IF EXISTS (SELECT 1 FROM loan WHERE id_book = OLD.id_book AND returned = false) THEN " +
                "        RAISE EXCEPTION 'Cannot delete book because it is currently on loan and not yet returned.'; " +
                "    END IF; " +
                "    RETURN OLD; " +
                "END; " +
                "$$ LANGUAGE plpgsql;";

        String createTrigger = "CREATE TRIGGER before_delete_book " +
                "BEFORE DELETE ON book " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION check_book_returned_before_deletion();";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTriggerFunction);
            stmt.execute(createTrigger);
            System.out.println("Delete book trigger created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDeletePatronTrigger(Connection connection) {
        //If the patron is loaning a book and the user want to delete the patron row on table.
        String createTriggerFunction = "CREATE OR REPLACE FUNCTION check_book_returned_before_deletion() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    IF EXISTS (SELECT 1 FROM loan WHERE id_patron = OLD.id_patron AND returned = false) THEN " +
                "        RAISE EXCEPTION 'Cannot delete patron because he/she is currently loaning a book.'; " +
                "    END IF; " +
                "    RETURN OLD; " +
                "END; " +
                "$$ LANGUAGE plpgsql;";

        String createTrigger = "CREATE TRIGGER before_delete_patron " +
                "BEFORE DELETE ON patron " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION check_book_returned_before_deletion();";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTriggerFunction);
            stmt.execute(createTrigger);
            System.out.println("Delete patron trigger created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

