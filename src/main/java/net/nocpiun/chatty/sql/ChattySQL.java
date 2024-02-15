package net.nocpiun.chatty.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ChattySQL {
    private Connection conn;
    public Statement statement;

    public ChattySQL(String server, String userName, String password) throws Exception {
        conn = DriverManager.getConnection("jdbc:mysql://"+ server, userName, password);
        statement = conn.createStatement();

        init();
    }

    private void init() throws Exception {
        // Create databases
        statement.execute("create database if not exists chatty_rooms character set utf8;");
        statement.execute("create database if not exists chatty_users character set utf8;");
        // Create tables
        statement.execute("use chatty_users;");
        statement.execute("create table if not exists user_info ( name varchar(50) not null, token varchar(36) not null, password varchar(100) not null );");
        statement.execute("create table if not exists user_profile ( name varchar(50) not null, rooms json not null );");
    }

    public void close() throws Exception {
        statement.close();
        conn.close();
    }
}
