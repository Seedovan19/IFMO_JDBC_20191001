package com.efimchick.ifmo.web.jdbc.service;

import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;

import java.util.List;
import java.util.Objects;

public class ServiceFactory {

    public EmployeeService employeeService(){
        return new EmployeeService() {
            @Override
            public List<Employee> getAllSortByHireDate(Paging paging) {
                return Helper.getPage("SELECT * FROM employee ORDER BY hireDate", paging);
            }

            @Override
            public List<Employee> getAllSortByLastname(Paging paging) {
                return Helper.getPage("SELECT * FROM employee ORDER BY lastName", paging);
            }

            @Override
            public List<Employee> getAllSortBySalary(Paging paging) {
                return Helper.getPage("SELECT * FROM employee ORDER BY salary", paging);
            }

            @Override
            public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
                return Helper.getPage("SELECT * FROM employee ORDER BY department, lastname", paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
                return Helper.getPage("SELECT * FROM employee WHERE department=" + department.getId() + "ORDER BY hireDate", paging);
            }

            @Override
            public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
                return Helper.getPage("SELECT * FROM employee WHERE department=" + department.getId() + " ORDER BY salary", paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
                return Helper.getPage("SELECT * FROM employee WHERE department=" + department.getId() + " ORDER BY lastName", paging);
            }

            @Override
            public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
                return Helper.getPage("SELECT * FROM employee WHERE manager=" + manager.getId() + " ORDER BY lastName", paging);
            }

            @Override
            public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
                return Helper.getPage("SELECT * FROM employee WHERE manager=" + manager.getId() + " ORDER BY hireDate", paging);
            }

            @Override
            public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
                return Helper.getPage("SELECT * FROM employee WHERE manager=" + manager.getId() + " ORDER BY salary", paging);
            }

            @Override
            public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
                return Objects.requireNonNull(Helper.getSortedEmployees(true, true, "SELECT * FROM employee WHERE id = " + employee.getId())).get(0);
            }

            @Override
            public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
                return Objects.requireNonNull(Helper.getSortedEmployees(false, true, "SELECT * FROM employee WHERE department=" + department.getId() + " ORDER BY salary DESC")).get(salaryRank-1);
            }
        };
    }
}
