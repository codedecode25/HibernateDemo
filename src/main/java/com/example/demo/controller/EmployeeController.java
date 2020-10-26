package com.example.demo.controller;


import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.HibernateUtil;
import com.example.demo.entities.Employee;
import com.example.demo.service.EmployeeService;
 
@RestController
@RequestMapping("/employees")
public class EmployeeController 
{
	
	@Autowired
    EmployeeService service;
	
	@GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> list = service.getAllEmployees();
 
        return new ResponseEntity<List<Employee>>(list, new HttpHeaders(), HttpStatus.OK);
    }
	
	@GetMapping("/entityLifeCycle")
    public void showEntityLifeCycle() {
         service.showEntityLifeCycle();
 
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) 
                                                    throws Exception {
        Employee entity = service.getEmployeeById(id);
 
        return new ResponseEntity<Employee>(entity, new HttpHeaders(), HttpStatus.OK);
    }
 
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Employee> createOrUpdateEmployee(@RequestBody Employee employee)
                                                    throws Exception {
    	//here employee object will be in new state.
    	 Session session = HibernateUtil.getSessionFactory().openSession();
		 session.beginTransaction();
		 
		//here employee object will be in new state hence its not in sesion / first level cache.
		 Boolean doesExist = session.contains(employee);
		 System.out.println(employee.getFirstName() + employee.getLastName()+ "exists status : " + doesExist);

		//here employee object will be in managed state as u can see query is fired.
    	 Employee employee2 = session.get(Employee.class, new Long(2));
    	 Boolean doesExistEmp2 = session.contains(employee2);
		 System.out.println(employee2.getFirstName() + employee2.getLastName()+ "exists status : " + doesExistEmp2);
		//after this its proved that employee object will be in managed state in the session.

		 //Query is not fired as sssion now has this object with id 2
		 Employee employee3 = session.get(Employee.class, new Long(2));
    	 Boolean doesExistEmp3 = session.contains(employee3);
		 System.out.println(employee3.getFirstName() + employee3.getLastName()+ "exists status : " + doesExistEmp3);
		
		 
//    	 System.out.println(employee2.getFirstName() + employee2.getLastName());
        Employee updated = service.createOrUpdateEmployee(employee);
        return new ResponseEntity<Employee>(updated, new HttpHeaders(), HttpStatus.OK);
    }
 
    @DeleteMapping("/{id}")
    public HttpStatus deleteEmployeeById(@PathVariable("id") Long id) 
                                                    throws Exception {
        service.deleteEmployeeById(id);
        return HttpStatus.FORBIDDEN;
    }
	
}