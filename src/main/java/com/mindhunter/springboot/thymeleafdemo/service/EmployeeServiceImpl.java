package com.mindhunter.springboot.thymeleafdemo.service;

import com.mindhunter.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.mindhunter.springboot.thymeleafdemo.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public Employee find(int theId) {
        Optional<Employee> result = employeeRepository.findById(theId);
        Employee thEmployee = null;
        if(result.isPresent()){
            thEmployee = result.get();
        }else{
            throw new RuntimeException("Did not find employee id - " + theId);
        }
        return thEmployee;
    }

    @Override
    public void save(Employee theEmployee) {
        employeeRepository.save(theEmployee);
    }

    @Override
    public void delete(int theId) {
        employeeRepository.deleteById(theId);
    }
}
