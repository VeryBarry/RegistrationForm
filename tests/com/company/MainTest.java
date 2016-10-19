package com.company;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public void testInsertUser() throws SQLException {
        Connection conn = startConnection();
        User u = new User(1, "Alice", "123 main st", "alice@gmail.com");
        Main.insertUser(conn, u);
        ArrayList<User> user = Main.selectUser(conn);
        conn.close();
        assertTrue(user != null);
    }
    @Test
    public void testUpdateUser() throws SQLException {
        Connection conn = startConnection();
        User u = new User(1, "Alice", "123 main st", "alice@gmail.com");
        Main.insertUser(conn, u);
        User up = new User(1, "bob", "123 main st", "alice@gmail.com");
        Main.updateUser(conn, up);
        ArrayList<User> user = Main.selectUser(conn);
        conn.close();
        assertTrue(user != null);
        assertTrue(user.contains("bob"));
    }
    @Test
    public void testDeleteUser() throws SQLException {
        Connection conn = startConnection();
        User u = new User(1, "Alice", "123 main st", "alice@gmail.com");
        Main.insertUser(conn, u);
        Main.deleteUser(conn, 1);
        ArrayList<User> user = Main.selectUser(conn);
        conn.close();
        assertTrue(user == null);
    }
}