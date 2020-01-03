package com.mindhunter.springboot.thymeleafdemo.service;


import com.mindhunter.springboot.thymeleafdemo.entity.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> findAll();

    public Employee find(int theId);

    public void save(Employee theEmployee);

    public void delete(int theId);

}
