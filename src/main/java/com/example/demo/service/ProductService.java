package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.MyException;
import com.example.demo.beans.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	@Autowired
	private ProductRepository productRepository;

	public Product saveProduct(Product product) {
		Product productFromDB = null;
		try {
			productFromDB = productRepository.save(product);
			if (productFromDB == null) {
				throw new MyException("Sorry Could Not Persisit Product In the Database");
			}
		} catch (final MyException ex) {
			logger.info("Exception:- " + ex.getMessage());
		}
		return productFromDB;
	}

	public Product getProductById(Long id) {
		Product product = null;
		try {
			product = productRepository.getProductById(id);
			if (product == null) {
				throw new MyException("Sorry Could Not Retieve Data Of Product From The Database");
			}
		} catch (final MyException ex) {
			logger.info("Exception :- " + ex.getMessage());
		}
		return product;
	}

	public List<Product> getAllProduct() {
		List<Product> listOfProduct = null;
		try {
			listOfProduct = productRepository.findAll();
			if (listOfProduct == null || listOfProduct.size() == 0) {
				throw new MyException("Sorry No Data Retrieved From The Database");
			}
		} catch (final MyException ex) {
			logger.info("Exception :- " + ex.getMessage());
		}
		return listOfProduct;
	}

	public String deleteProductById(Long id) {
		String response = null;
		try {
			productRepository.deleteById(id);
			response = "SuccessFully Deleted";
		} catch (final MyException ex) {
			logger.info("Exception :-" + ex.getMessage());
		}
		return response;
	}
}
