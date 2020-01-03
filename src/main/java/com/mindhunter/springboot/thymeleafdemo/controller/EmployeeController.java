package com.mindhunter.springboot.thymeleafdemo.controller;

import com.mindhunter.springboot.thymeleafdemo.entity.Employee;
import com.mindhunter.springboot.thymeleafdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public String listEmployees(
            Model thModel
            ){
        List<Employee> theEmployees = employeeService.findAll();
        thModel.addAttribute("employeesList", theEmployees);
        return "employees/list-employees";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel){
        Employee employee = new Employee();
        theModel.addAttribute("employee", employee);

        return "employees/employee-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int theId,
                                    Model theModel){
        Employee employee = employeeService.find(theId);
        theModel.addAttribute("employee", employee);
        return "employees/employee-form";
    }

    @GetMapping("/delete")
    public String showFormForUpdate(@RequestParam("employeeId") int theId){
        employeeService.delete(theId);
        return "redirect:/employees/list";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("employee") Employee employee){
        employeeService.save(employee);
        return "redirect:/employees/list";
    }

}
