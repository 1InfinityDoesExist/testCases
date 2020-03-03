package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.MyException;
import com.example.demo.beans.Customer;
import com.example.demo.repository.CustomerRepository;

@Service
public class CustomerService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository customerRespository;

	public Customer saveCustoreDetails(Customer customer) {
		Customer customerFromDB = null;

		try {
			customerFromDB = customerRespository.save(customer);
			if (customerFromDB == null) {
				throw new MyException("Sorry Could Not Persist Data In The Database:-");
			}
		} catch (final Exception ex) {
			logger.info("*****************Exception Message:- " + ex.getMessage());
		}
		return customerFromDB;

	}

	public Customer getCustomerById(Long id) {
		Customer customer = null;
		try {
			customer = customerRespository.getCustomerById(id);
			if (customer == null) {
				throw new MyException("Sorry Could Not Retrieve Data From The Database");
			}
		} catch (final MyException ex) {
			logger.info("*****************Exception Message:- " + ex.getMessage());
		}
		return customer;
	}

	public List<Customer> getAllCustomer() {
		List<Customer> listOfCustomer = null;
		try {
			listOfCustomer = customerRespository.findAll();
			if (listOfCustomer.size() == 0 || listOfCustomer == null) {
				throw new MyException("Sorry Could Not Retrieve Data From The DB");
			}
		} catch (final MyException ex) {
			logger.info(ex.getMessage());
		}
		return listOfCustomer;
	}

	public String deleteCustomerById(Long id) {

		String response = null;
		try {
			customerRespository.deleteById(id);
			response = "Successfully Deleted";
		} catch (final MyException ex) {
			logger.info("Exception:- " + ex.getMessage());
		}
		return response;

	}
}