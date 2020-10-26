package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.HibernateUtil;
import com.example.demo.entities.Employee;
import com.example.demo.repositories.EmployeeRepo;

@Service
public class EmployeeService {
	
	 @Autowired
	 EmployeeRepo repository;
	 
	
	 
	 
	 public List<Employee> getAllEmployees()
	 {
		 Session session = HibernateUtil.getSessionFactory().openSession();
		 session.beginTransaction();

		 Employee employee = session.get(Employee.class, new Long(2));
		 System.out.println(employee.getFirstName() + employee.getLastName());

		 List<Employee> employeeList = repository.findAll();

		 Employee employee1 = (Employee)session.get(Employee.class, new Long(2));
		 System.out.println(employee1.getFirstName() + employee1.getLastName());

		 if(employeeList.size() > 0) {
			 return employeeList;
		 } else {
			 return new ArrayList<Employee>();
		 }
	 }

	    public Employee getEmployeeById(Long id) throws Exception 
	    {
	        Optional<Employee> employee = repository.findById(id);
	         
	        if(employee.isPresent()) {
	            return employee.get();
	        } else {
	            throw new Exception("No employee record exist for given id");
	        }
	    }
	     
	    public Employee createOrUpdateEmployee(Employee entity) throws Exception 
	    {
	    	if(entity.getId() == null) {
	    		//It comes in managed state.
	    		entity = repository.save(entity);
	             
	            return entity;
	    	}
	    	//It comes in managed state.
	        Optional<Employee> employee = repository.findById(entity.getId());
	         
	        if(employee.isPresent()) 
	        {
	            Employee newEntity = employee.get();
	            newEntity.setEmail(entity.getEmail());
	            newEntity.setFirstName(entity.getFirstName());
	            newEntity.setLastName(entity.getLastName());
	 
	            newEntity = repository.save(newEntity);
	             
	            return newEntity;
	        } else {
	            entity = repository.save(entity);
	             
	            return entity;
	        }
	    } 
	     
	    public void deleteEmployeeById(Long id) throws Exception 
	    {
	        Optional<Employee> employee = repository.findById(id);
	         
	        if(employee.isPresent()) 
	        {
	            repository.deleteById(id);
	        } else {
	            throw new Exception("No employee record exist for given id");
	        }
	    }

		public void showEntityLifeCycle() {
			
			 Session session = HibernateUtil.getSessionFactory().openSession();
			 session.beginTransaction();

			 Employee employee = session.get(Employee.class, new Long(2));
			 System.out.println(employee.getFirstName() + employee.getLastName());
			 
			 Boolean doesExist = session.contains(employee);
			 System.out.println(employee.getFirstName() + employee.getLastName()+ "exists status : " + doesExist);

			 //session cleared, it comes in detached state 
			 session.clear();
			 Boolean doesExistAfterCleared = session.contains(employee);
			 System.out.println(employee.getFirstName() + employee.getLastName()+ "exists status : " + doesExistAfterCleared);

			 //remove object it goes into removed state.
			 session.delete(employee);
			 Boolean doesExistAfterRemoved = session.contains(employee);
			 System.out.println(employee.getFirstName() + employee.getLastName()+ "exists status : " + doesExistAfterRemoved);
			 // this is the diff between JPA and hibernate -> hibernate allows us to remove even the detached entities.
		}

}
