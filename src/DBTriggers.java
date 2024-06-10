import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTriggers {

    public static void createTrigger(Connection connection) {
        String createTriggerFunction = "CREATE OR REPLACE FUNCTION check_book_availability() RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    IF EXISTS (SELECT 1 FROM loan WHERE id_book = NEW.id_book AND Returned = false) THEN " +
                "        RAISE EXCEPTION 'The book is already loaned out and not yet returned.'; " +
                "    END IF; " +
                "    RETURN NEW; " +
                "END; " +
                "$$ LANGUAGE plpgsql;";

        String createTrigger = "CREATE TRIGGER before_insert_loan " +
                "BEFORE INSERT ON loan " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION check_book_availability();";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTriggerFunction);
            stmt.execute(createTrigger);
            System.out.println("Trigger created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
