package util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

import com.serenitask.util.DatabaseManager.SqliteConnection;

// DBConnection_Test class tests the DBConnection class
public class DBConnectionTest {
    @Test
    public void testGetConnection() {
        Connection connection = SqliteConnection.getConnection();
        assertNotNull(connection, "Connection should not be null");
    }
}