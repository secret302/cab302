package dao;

import com.serenitask.util.DatabaseManager.SqliteConnection;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

// SqliteConnectionTest class tests the SqliteConnection class
public class SqliteConnectionTest {
    @Test // Test the connection to the SQLite database
    public void testConnection() {
        // Create a new SQLite connection
        Connection connection = SqliteConnection.getConnection();
        // Check if the connection is not null
        assertNotNull(connection);
    }
}
