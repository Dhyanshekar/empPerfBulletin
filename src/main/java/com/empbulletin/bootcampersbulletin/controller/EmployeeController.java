package com.empbulletin.bootcampersbulletin.controller;

import java.util.List;

import DTO.EmployeeDTO;
import com.empbulletin.bootcampersbulletin.exception.ResourceNotFoundException;
import com.empbulletin.bootcampersbulletin.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import com.empbulletin.bootcampersbulletin.model.Employee;
import com.empbulletin.bootcampersbulletin.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1/employees")

public class EmployeeController {

	@Autowired
	private EmployeeRepository eR;
	@Autowired
	private EmployeeService employeeService;
	private java.util.stream.Collectors Collectors;


	//getting all the employee details
	@GetMapping
	public List<EmployeeDTO> getAllEmployees() {
		List<Employee> employees = eR.findAll();
		return employees.stream()
				.map(employee -> {
					EmployeeDTO dto = new EmployeeDTO();
					dto.setEmp_id(employee.getEmp_id());
					dto.setEmp_name(employee.getEmp_name());
					dto.setEmp_mail(employee.getEmp_mail());
					dto.setBatchNo(employee.getBatchNo());
					return dto;
				})
				.collect(Collectors.toList());
	}
	// getting employee details by id
	@GetMapping("/{id}")
	public EmployeeDTO getEmployeeById(@PathVariable Long id) {
		Optional<Employee> employee = eR.findById(id);
		if (employee.isPresent()) {
			Employee emp = employee.get();
			EmployeeDTO dto = new EmployeeDTO();
			dto.setEmp_id(emp.getEmp_id());
			dto.setEmp_name(emp.getEmp_name());
			dto.setEmp_mail(emp.getEmp_mail());
			dto.setBatchNo(emp.getBatchNo());
			return dto;
		} else {
			throw new ResourceNotFoundException("Employee with id " + id + " not found");
		}
	}

	//getting employees list by batch number
	@GetMapping("/batch/{batchNo}")
	public List<EmployeeDTO> getEmployeesByBatchNo(@PathVariable Integer batchNo) {
		List<Employee> employees = eR.findByBatchNo(batchNo);
		if (employees.isEmpty()) {
			throw new ResourceNotFoundException("Employees in batch number " + batchNo + " not found");
		}
		return employees.stream()
				.map(employee -> {
					EmployeeDTO dto = new EmployeeDTO();
					dto.setEmp_id(employee.getEmp_id());
					dto.setEmp_name(employee.getEmp_name());
					dto.setEmp_mail(employee.getEmp_mail());
					dto.setBatchNo(employee.getBatchNo());
					return dto;
				})
				.collect(Collectors.toList());
	}


	//employee login

	@PostMapping("/login")
	public String login(@RequestParam Long emp_id, @RequestParam String password) {
		if (employeeService.authenticate(emp_id, password)) {
			return "Login successful";
		} else {
			return "Login failed";
		}
	}



}
