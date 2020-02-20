package com.efimchick.ifmo.web.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

public class SetMapperFactory {

    public SetMapper<Set<Employee>> employeesSetMapper() {
        SetMapper<Set<Employee>> setMapper = new SetMapper<Set<Employee>>() {
            @Override
            public Set<Employee> mapSet(ResultSet rs) {
                try {
                    Set<Employee> employees = new HashSet<Employee>();
                    while (rs.next()) {
                        Employee empl = getEmpl(rs);
                        employees.add(empl);
                    }
                    return employees;
                } catch (SQLException e) {
                    return null;
                }
            }
        };
        return setMapper;
    };

    private Employee getEmpl(ResultSet rs) throws SQLException {
        try {
            BigInteger id = new BigInteger(rs.getString("id"));
            FullName fullName = new FullName(
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("middleName")
            );
            Position pos = Position.valueOf(rs.getString("position"));
            LocalDate date = LocalDate.parse(rs.getString("hireDate"));
            BigDecimal salary = rs.getBigDecimal("salary");
            Employee man = getMan(rs);
            return new Employee(id, fullName, pos, date, salary, man);
        } catch (SQLException e) {
            return null;
        }
    }

    private Employee getMan(ResultSet rs) throws SQLException {
        try {
            Employee man = null;
            if (rs.getString("manager") != null) {
                int managerId = Integer.parseInt(rs.getString("manager"));
                int rowBeforeManager = rs.getRow();

                rs.beforeFirst();
                while (rs.next() && man == null) {
                    if (rs.getInt("ID") == managerId) {
                        man = getEmpl(rs);
                    }
                }
                rs.absolute(rowBeforeManager);
            }
            return man;
        } catch (SQLException e) {
            return null;
        }

    }
}
