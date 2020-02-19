package com.efimchick.ifmo.web.jdbc;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;
import java.math.BigInteger;
import java.time.LocalDate;

public class RowMapperFactory {

    public RowMapper<Employee> employeeRowMapper() {
        //throw new UnsupportedOperationException();
        RowMapper<Employee> rowMapper = new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs) {
                try {
                    BigInteger id = new BigInteger(rs.getString("id"));
                    FullName fullName = new FullName(
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("middleName")
                    );
                    Position pos = Position.valueOf(rs.getString("position"));
                    LocalDate hireDate = LocalDate.parse(rs.getString("hireDate"));
                    BigDecimal salary = rs.getBigDecimal("salary");

                    return new Employee(id, fullName, pos, hireDate, salary);
                } catch (SQLException e) {
                    throw new UnsupportedOperationException();
                }
            }
        };
        return rowMapper;

    }
}
