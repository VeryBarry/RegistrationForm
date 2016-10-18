package com.company;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTable(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                (request, response) -> {
                    ArrayList<User> users = selectUser(conn);
                    JsonSerializer serializer = new JsonSerializer();
                    MessagesWrapper wrapper = new MessagesWrapper(messages);
                    return serializer.deep(true).serialize(wrapper);
                }
        );
        Spark.post(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    HashMap<String, String> msg = parser.parse(body);
                    insertUser(conn, , 0);
                    return null;
                }
        );
        Spark.put(
                "/user",
                (request, response) -> {

                    return null;
                }
        );
        Spark.delete(
                "/user/:id",
                (request, response) -> {

                    return null;
                }
        );
    }
    static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }
    public static void insertUser(Connection conn, String name, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES(NULL, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }
    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            return new User(id, username, address, email);
        }
        return null;
    }
    public static void updateUser(Connection conn) {

    }
    public static void deleteUser(Connection conn) {

    }
}
