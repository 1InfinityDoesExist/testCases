package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.MyException;
import com.example.demo.beans.CustomerProduct;
import com.example.demo.repository.CustomerProductRepository;

@Service
public class CustomerProductService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerProductService.class);

	@Autowired
	private CustomerProductRepository custoproRepository;

	public CustomerProduct createCusotmerProduct(CustomerProduct custoPro) {
		CustomerProduct customerProduct = null;

		try {
			customerProduct = custoproRepository.save(custoPro);
			if (customerProduct == null) {
				throw new MyException("Sorry Could Not Persist Data In The Database");
			}
		} catch (final MyException ex) {
			logger.info(ex.getMessage());
		}
		return customerProduct;
	}

	public CustomerProduct getCustomerProductById(Long id) {
		CustomerProduct customerProduct = null;
		try {
			customerProduct = custoproRepository.getCustomerProductById(id);
			if (customerProduct == null) {
				throw new MyException("Sorry No Data Found For This Id");
			}
		} catch (final MyException ex) {
			logger.info("Exception:- " + ex.getMessage());
		}
		return customerProduct;
	}

	public List<CustomerProduct> getCustomerProductAll() {
		List<CustomerProduct> listOfCustomerProduct = null;
		try {
			listOfCustomerProduct = custoproRepository.findAll();
			if (listOfCustomerProduct == null || listOfCustomerProduct.size() == 0) {
				throw new MyException("Sorry No Data Found For For CustomerProduct");
			}
		} catch (final MyException ex) {
			logger.info(ex.getMessage());
		}
		return listOfCustomerProduct;
	}

	public void deleteCustoProduct(Long id) {
		try {
			custoproRepository.deleteById(id);
		} catch (final MyException ex) {
			logger.info("Exception:-" + ex.getMessage());
		}

	}
}
