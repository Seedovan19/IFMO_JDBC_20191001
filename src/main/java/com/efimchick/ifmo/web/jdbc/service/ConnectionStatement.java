package com.efimchick.ifmo.web.jdbc.service;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionStatement {
    private Connection con;
    private Statement  stmt;

    public ConnectionStatement() throws SQLException {
        this.con = getConnection();
        this.stmt = this.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public Connection getConnection() throws SQLException {
        return ConnectionSource.instance().createConnection();
    }

    public Statement getStmt() {
        return this.stmt;
    }

    public void closeConAndStmt() throws SQLException {
        this.stmt.close();
        this.con.close();
        this.stmt = null;
        this.con = null;
    }
}
