package com.efimchick.ifmo.web.jdbc.service;

import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Helper {

    public static ResultSet getResultSet(String query) throws SQLException {
        ConnectionStatement conStmt = new ConnectionStatement();
        return conStmt.getStmt().executeQuery(query);
    }

    public static List<Employee> getSortedEmployees(boolean chain, boolean isManagerNeeded, String query) {
        try {
            List<Employee> listOfEmployees = new LinkedList<>();
            ResultSet rs = getResultSet(query);
            while (rs.next()) {
                Employee employee = getEmployee(rs, chain, isManagerNeeded);
                listOfEmployees.add(employee);
            }
            return listOfEmployees;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Department getDepartmentById(BigInteger id) throws SQLException {
        try {
            Department dep = null;
            ResultSet rs = getResultSet("SELECT * FROM DEPARTMENT where id=" + id);
            while (rs.next()) {
                dep = getDepartment(rs);
            }
            return dep;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Employee getEmployee(ResultSet rs, boolean chain, boolean isManagerNeeded) throws SQLException {
        BigInteger id = new BigInteger(rs.getString("id"));
        FullName fullName = new FullName(
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("middleName")
        );
        Position pos = Position.valueOf(rs.getString("position"));
        LocalDate date = LocalDate.parse(rs.getString("hireDate"));
        BigDecimal salary = rs.getBigDecimal("salary");
        Employee man = null;
        Department dep = null;
        if (rs.getObject("manager") != null) {
            if (chain || isManagerNeeded) {
                BigInteger managerId = new BigInteger(rs.getString("manager"));
                String query = "SELECT * FROM employee WHERE id=" + managerId;

                man = Objects.requireNonNull(getSortedEmployees(chain, false, query)).get(0);
            }
        }
        if (rs.getObject("department") != null) {
            BigInteger departmentId = BigInteger.valueOf(rs.getInt("department"));
            dep = getDepartmentById(departmentId);
        }
        return new Employee(id, fullName, pos, date, salary, man, dep);
    }

    public static Department getDepartment(ResultSet rs) throws SQLException {
        return new Department(new BigInteger(rs.getString("id")), rs.getString("name"), rs.getString("location"));
    }

    public static List<Employee> getPage(String query, Paging paging) {
        List<Employee> list = getSortedEmployees(false, true, query);
        assert list != null;
        return list.subList(Math.max((paging.page - 1) * paging.itemPerPage, 0), Math.min((paging.page) * paging.itemPerPage, list.size()));
    }
}
