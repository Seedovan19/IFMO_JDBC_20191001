package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;

import java.math.BigInteger;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DaoFactory {
    public EmployeeDao employeeDAO() {
        //throw new UnsupportedOperationException();
        EmployeeDao empDao = new EmployeeDao() {
            @Override
            public List<Employee> getByDepartment(Department department) {
                Connection con = null;
                PreparedStatement stmt = null;
                List<Employee> employeesByDepartment = new LinkedList<Employee>();

                try {
                    con = Helpers.getConnection();
                    String query = "SELECT * FROM employee WHERE department=?";
                    stmt = con.prepareStatement(query);
                    stmt.setInt(1, department.getId().intValue());
                    ResultSet rs = stmt.executeQuery();

                    while(rs.next()) {
                        employeesByDepartment.add(Helpers.getEmpl(rs));
                    }

                    return employeesByDepartment;
                } catch (SQLException e) {
                    return null;
                } finally {
                    Helpers.closeWithPrep(stmt, con);
                }
            }

            @Override
            public List<Employee> getByManager(Employee employee) {
                Connection con = null;
                PreparedStatement stmt = null;
                List<Employee> employeesByManager = new LinkedList<Employee>();

                try {
                    con = Helpers.getConnection();
                    String query = "SELECT * FROM employee WHERE manager=?";
                    stmt = con.prepareStatement(query);
                    stmt.setInt(1, employee.getId().intValue());
                    ResultSet rs = stmt.executeQuery();

                    while(rs.next()) {
                        employeesByManager.add(Helpers.getEmpl(rs));
                    }

                    return employeesByManager;
                } catch (SQLException e) {
                    return null;
                } finally {
                    Helpers.closeWithPrep(stmt, con);
                }
            }

            @Override
            public Optional<Employee> getById(BigInteger Id) {
                Connection con = null;
                PreparedStatement stmt = null;
                Optional employee = Optional.empty();
                try {
                    con = Helpers.getConnection();
                    String query = "SELECT * FROM employee WHERE id=?";
                    stmt = con.prepareStatement(query);
                    stmt.setInt(1, Id.intValue());
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        employee = Optional.of(Helpers.getEmpl(rs));
                    }
                    return employee;
                } catch (SQLException e) {
                    return Optional.empty();
                } finally {
                    Helpers.closeWithPrep(stmt, con);
                }
            }

            @Override
            public List<Employee> getAll() {
                List<Employee> listOfEmployees = new LinkedList<>();
                try {
                    String query = "SELECT * FROM employee";
                    ResultSet rs = Helpers.getResultSet(query);
                    assert rs != null;
                    while (rs.next()) {
                        Employee employee = Helpers.getEmpl(rs);
                        listOfEmployees.add(employee);
                    }
                    return listOfEmployees;
                } catch (SQLException e) {
                    return null;
                }
            }

            @Override
            public Employee save(Employee employee) {
                Connection con = null;
                PreparedStatement stmt = null;
                try {
                    con = Helpers.getConnection();
                    String query = "INSERT INTO employee VALUES (?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(query);
                    stmt.setInt(1, employee.getId().intValue());
                    stmt.setString(2, employee.getFullName().getFirstName());
                    stmt.setString(3, employee.getFullName().getLastName());
                    stmt.setString(4, employee.getFullName().getMiddleName());
                    stmt.setString(5, employee.getPosition().toString());
                    stmt.setInt(6, employee.getManagerId().intValue());
                    stmt.setDate(7, Date.valueOf(employee.getHired()));
                    stmt.setDouble(8, employee.getSalary().doubleValue());
                    stmt.setInt(9, employee.getDepartmentId().intValue());

                    stmt.executeUpdate();
                    return employee;
                } catch (SQLException e) {
                    return null;
                } finally {
                    Helpers.closeWithPrep(stmt, con);
                }
            }

            @Override
            public void delete(Employee employee) {
                Connection con = null;
                PreparedStatement stmt = null;
                try {
                    con = Helpers.getConnection();
                    String sql = "DELETE FROM employee WHERE id=?";
                    stmt = con.prepareStatement(sql);
                    stmt.setInt(1, employee.getId().intValue());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    Helpers.closeWithPrep(stmt, con);
                }
            }
        };
        return empDao;
    }

    public DepartmentDao departmentDAO() {
        DepartmentDao depDao = new DepartmentDao() {
            @Override
            public Optional<Department> getById(BigInteger Id) {
                Connection con = null;
                PreparedStatement stmt = null;
                Optional<Department> department = Optional.empty();
                try {
                    con = Helpers.getConnection();
                    String query = "SELECT * FROM department WHERE id=?";
                    stmt = con.prepareStatement(query);
                    stmt.setInt(1, Id.intValue());
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        department = Optional.of(Helpers.getDep(rs));
                    }

                    return department;
                } catch (SQLException e) {
                    return Optional.empty();
                } finally {
                    Helpers.closeWithPrep(stmt, con);
                }
            }

            @Override
            public List<Department> getAll() {
                try {
                    List<Department> listOfDep = new LinkedList<>();
                    ResultSet rs = Helpers.getResultSet("SELECT * FROM department");
                    assert rs != null;
                    while (rs.next()) {
                        Department department = Helpers.getDep(rs);
                        listOfDep.add(department);
                    }
                    return listOfDep;
                } catch (SQLException e) {
                    return null;
                }
            }

            @Override
            public Department save(Department department) {
                Connection con = null;
                PreparedStatement stmt = null;
                try {
                    con = Helpers.getConnection();
                    if (getById(department.getId()).isPresent()) {
                        stmt = Helpers.updateDep(con, department);
                    } else {
                        stmt = Helpers.insertDep(con, department);
                    }
                    stmt.executeUpdate();
                    return department;

                } catch (SQLException e) {
                    return null;
                } finally {
                    System.out.println("finally");
                    Helpers.closeWithPrep(stmt, con);
                }
            }

            @Override
            public void delete(Department department) {
                Connection con = null;
                PreparedStatement stmt = null;
                try {
                    con = Helpers.getConnection();
                    String query = "DELETE FROM department WHERE id=?";
                    stmt = con.prepareStatement(query);
                    stmt.setString(1, department.getId().toString());
                    stmt.executeUpdate();
                    return;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                } finally {
                    Helpers.closeWithPrep(stmt, con);
                }
            }
        };
        return depDao;
    }
}
