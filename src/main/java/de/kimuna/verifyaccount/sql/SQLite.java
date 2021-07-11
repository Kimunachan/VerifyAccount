package de.kimuna.verifyaccount.sql;


import java.sql.*;

public class SQLite {
    private Connection connection = null;
    private Statement statement;

    public SQLite() {
        connect();
        init();
    }

    public void init() {
        onUpdate("CREATE TABLE IF NOT EXISTS users(user_id TEXT NOT NULL, second_id TEXT NOT NULL, verified INTEGER);");
    }

    public void connect() {
        String url = "jdbc:sqlite:data.db";
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url);
                statement = connection.createStatement();
                statement.setQueryTimeout(3);
                System.out.println("Database Connected!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


    public ResultSet onQuery(String query) {
        try {
            connect();
            return statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void onUpdate(String query) {
        try {
            connect();
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database Connection closed!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}

