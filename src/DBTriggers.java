import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTriggers {

    public static void createTriggers(Connection connection) {
        String createTrigger = "CREATE OR REPLACE FUNCTION check_loan_before_insert()\n" +
                "RETURNS TRIGGER AS $$\n" +
                "BEGIN\n" +
                "    -- Check if the patron already has an unreturned book\n" +
                "    IF EXISTS (\n" +
                "        SELECT 1\n" +
                "        FROM loan\n" +
                "        WHERE id_patron = NEW.id_patron\n" +
                "        AND loaning = TRUE\n" +
                "        AND return_date IS NULL\n" +
                "    ) THEN\n" +
                "        RAISE EXCEPTION 'Patron has an unreturned book. Cannot loan another book until the previous one is returned.';\n" +
                "    END IF;\n" +
                "\n" +
                "    -- Check if the book is already on loan\n" +
                "    IF EXISTS (\n" +
                "        SELECT 1\n" +
                "        FROM loan\n" +
                "        WHERE id_book = NEW.id_book\n" +
                "        AND loaning = TRUE\n" +
                "        AND return_date IS NULL\n" +
                "    ) THEN\n" +
                "        RAISE EXCEPTION 'Book is already on loan. Cannot loan this book until it is returned.';\n" +
                "    END IF;\n" +
                "\n" +
                "    RETURN NEW;\n" +
                "END;\n" +
                "$$ LANGUAGE plpgsql;\n" +
                "\n" +
                "CREATE TRIGGER check_loan_trigger\n" +
                "BEFORE INSERT ON loan\n" +
                "FOR EACH ROW\n" +
                "EXECUTE FUNCTION check_loan_before_insert();";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTrigger);
            System.out.println("Triggers created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}