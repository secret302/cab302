package utils;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

import utils.DBConnection;

// DBConnection_Test class tests the DBConnection class
public class DBConnection_Test {
    @Test
    public void testGetConnection() {
        Connection connection = DBConnection.getConnection();
        assertNotNull(connection, "Connection should not be null");
    }
}