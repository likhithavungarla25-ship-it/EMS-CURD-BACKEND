package com.example.ems.controller;

import com.example.ems.entity.Employee;
import com.example.ems.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void createEmployee_shouldReturnCreatedEmployee() {
        Employee employee = new Employee();
        employee.setName("Alice");
        employee.setDepartment("IT");
        employee.setSalary(50000);

        when(employeeService.saveEmployee(employee)).thenReturn(employee);

        Employee result = employeeController.createEmployee(employee);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(employeeService, times(1)).saveEmployee(employee);
    }

    @Test
    void getAllEmployees_shouldReturnEmployeeList() {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Alice");
        employee1.setDepartment("IT");
        employee1.setSalary(50000);

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Bob");
        employee2.setDepartment("HR");
        employee2.setSalary(60000);

        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> result = employeeController.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Alice");
        employee.setDepartment("IT");
        employee.setSalary(50000);

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        Employee result = employeeController.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getName());
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void updateEmployee_shouldReturnUpdatedEmployee() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setName("Alice Updated");
        updatedEmployee.setDepartment("Engineering");
        updatedEmployee.setSalary(70000);

        when(employeeService.updateEmployee(1L, updatedEmployee)).thenReturn(updatedEmployee);

        Employee result = employeeController.updateEmployee(1L, updatedEmployee);

        assertEquals("Alice Updated", result.getName());
        assertEquals("Engineering", result.getDepartment());
        assertEquals(70000, result.getSalary());
        verify(employeeService, times(1)).updateEmployee(1L, updatedEmployee);
    }

    @Test
    void deleteEmployee_shouldReturnSuccessMessage() {
        doNothing().when(employeeService).deleteEmployee(1L);

        String result = employeeController.deleteEmployee(1L);

        assertEquals("Employee deleted successfully", result);
        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}

