package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;

public class Helpers {

    public static Connection getConnection() throws SQLException {
        return ConnectionSource.instance().createConnection();
    }

    public static PreparedStatement updateDep(Connection con, Department dep) throws SQLException {
        PreparedStatement stmt = null;
        String query = "UPDATE department SET Name =?, Location =? WHERE Id =?";
        stmt = con.prepareStatement(query);
        stmt.setInt(3, dep.getId().intValue());
        stmt.setString(1, dep.getName());
        stmt.setString(2, dep.getLocation());
        return stmt;
    }

    public static PreparedStatement insertDep(Connection con, Department dep) throws SQLException {
        PreparedStatement stmt = null;
        String query = "INSERT INTO department VALUES (?, ?, ?)";
        stmt = con.prepareStatement(query);
        stmt.setInt(1, dep.getId().intValue());
        stmt.setString(2, dep.getName());
        stmt.setString(3, dep.getLocation());
        return stmt;
    }

        public static void closeWithPrep(PreparedStatement stmt, Connection con){
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeWithStmt(Statement stmt, Connection con){
        try {
            assert stmt != null;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getResultSet(String sql) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            return null;
        } finally {
            closeWithStmt(stmt, con);
        }
    }

    public static Employee getEmpl(ResultSet rs) throws SQLException {
        BigInteger id = new BigInteger(rs.getString("id"));
        FullName fullName = new FullName(
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("middleName")
        );
        Position pos = Position.valueOf(rs.getString("position"));
        LocalDate date = LocalDate.parse(rs.getString("hireDate"));
        BigDecimal salary = rs.getBigDecimal("salary");
        BigInteger man = null;

        if (rs.getObject("manager") != null) {
            man = new BigInteger(rs.getString("manager"));
        } else {
            man = BigInteger.valueOf(0);
        }

        BigInteger dep = null;

        if (rs.getObject("department") != null) {
            dep = new BigInteger(rs.getString("department"));
        } else {
            dep = BigInteger.valueOf(0);
        }

        return new Employee(id, fullName, pos, date, salary, man, dep);
    }

    public static Department getDep(ResultSet rs) throws SQLException {
        return new Department(new BigInteger(rs.getString("id")), rs.getString("name"), rs.getString("location"));
    }

}
