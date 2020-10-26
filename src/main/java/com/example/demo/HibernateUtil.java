package com.example.demo;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Employee;

@Component
public class HibernateUtil {
	
	@Value("${db.driver}")
	private  String DRIVER;
 
	@Value("${spring.datasource.password}")
	private  String PASSWORD;
 
	@Value("${spring.datasource.url}")
	private  String URL;
 
	@Value("${spring.datasource.username}")
	private  String USERNAME;
 
	@Value("${spring.jpa.properties.hibernate.dialect}")
	private  String DIALECT;
 
	@Value("${spring.jpa.show-sql}")
	private  String SHOW_SQL;
 
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private  String HBM2DDL_AUTO;
 
	@Value("${entitymanager.packagesToScan}")
	private  String PACKAGES_TO_SCAN;
 
	

    private static SessionFactory sessionFactory;
    
    public static  SessionFactory getSessionFactory() {
    	return sessionFactory;
    }
    
    @PostConstruct
    public  void setSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, DRIVER);
                settings.put(Environment.URL, URL);
                settings.put(Environment.USER, USERNAME);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, DIALECT);

                settings.put(Environment.SHOW_SQL, SHOW_SQL);

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                settings.put(Environment.HBM2DDL_AUTO, HBM2DDL_AUTO);

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Employee.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}