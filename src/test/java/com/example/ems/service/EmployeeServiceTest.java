package com.example.ems.service;

import com.example.ems.entity.Employee;
import com.example.ems.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void saveEmployee_shouldReturnSavedEmployee() {
        Employee employee = new Employee();
        employee.setName("Alice");
        employee.setDepartment("IT");
        employee.setSalary(50000);

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.saveEmployee(employee);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void getAllEmployees_shouldReturnListOfEmployees() {
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

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_shouldReturnEmployeeWhenExists() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Alice");
        employee.setDepartment("IT");
        employee.setSalary(50000);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeService.getEmployeeById(99L));

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(99L);
    }

    @Test
    void updateEmployee_shouldUpdateEmployeeDataAndSave() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setName("Alice");
        existingEmployee.setDepartment("IT");
        existingEmployee.setSalary(50000);

        Employee updatedDetails = new Employee();
        updatedDetails.setName("Alice Updated");
        updatedDetails.setDepartment("Engineering");
        updatedDetails.setSalary(70000);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        Employee result = employeeService.updateEmployee(1L, updatedDetails);

        assertEquals("Alice Updated", result.getName());
        assertEquals("Engineering", result.getDepartment());
        assertEquals(70000, result.getSalary());
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    void deleteEmployee_shouldDeleteEmployeeWhenExists() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Alice");
        employee.setDepartment("IT");
        employee.setSalary(50000);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).delete(employee);
    }
}

