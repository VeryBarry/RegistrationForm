package com.company;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by VeryBarry on 10/17/16.
 */
public class MainTest {
    Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTable(conn);
        return conn;
    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "123 main st", "alice@gmail.com");
        User user = Main.selectUser(conn, "Alice");
        conn.close();
        assertTrue(user != null);
        assertTrue(user.username.equals("Alice"));
    }
}