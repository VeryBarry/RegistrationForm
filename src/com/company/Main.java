package com.company;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

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
                    return serializer.serialize(users);
                }
        );
        Spark.post(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    insertUser(conn, user);
                    return "";
                }
        );
        Spark.put(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    updateUser(conn, user);
                    return "";
                }
        );
        Spark.delete(
                "/user/:id",
                (request, response) -> {
                    JsonParser p = new JsonParser();
                    Integer id = p.parse(request.params(":id"));
                    deleteUser(conn, id);
                    return "";
                }
        );
    }
    static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }
    public static void insertUser(Connection conn, User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES(NULL, ?, ?, ?)");
        stmt.setString(1, user.username);
        stmt.setString(2, user.address);
        stmt.setString(3, user.email);
        stmt.execute();
    }
    public static ArrayList<User> selectUser(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        ArrayList<User> users = new ArrayList<>();
        if (results.next()) {
            int id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            User u =  new User(id, username, address, email);
            users.add(u);
        }
        return users;
    }
    public static void updateUser(Connection conn, User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username = ?, address = ?, email = ? WHERE id = ?");
        stmt.setString(1, user.username);
        stmt.setString(2, user.address);
        stmt.setString(3, user.email);
        stmt.setInt(4, user.id);
        stmt.execute();
    }
    public static void deleteUser(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }
}
