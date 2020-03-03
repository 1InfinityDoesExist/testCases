package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.Product;
import com.example.demo.service.ProductService;
import com.example.demo.util.ReflectionUtil;

@RestController
@RequestMapping(path = "/object/product/")
@CrossOrigin
public class ProductRestController {

	@Autowired
	private ProductService productService;
	ReflectionUtil refUtil = ReflectionUtil.getInstance();

	@PostMapping(path = "create")
	public ResponseEntity<?> saveProduct(@Valid @RequestBody Product product, BindingResult result) {

		if (result != null && result.hasErrors()) {
			Map<String, String> error = new HashMap<String, String>();
			for (FieldError fieldError : result.getFieldErrors()) {
				error.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(error, HttpStatus.BAD_REQUEST);
		}
		Product p = productService.saveProduct(product);
		return new ResponseEntity<Product>(p, HttpStatus.CREATED);
	}

	@GetMapping(path = "get/{id}")
	public ResponseEntity<?> getProductById(@PathVariable(value = "id") Long id) {
		Product product = productService.getProductById(id);
		if (product == null) {
			return new ResponseEntity<String>("No product with Id:- " + id + "Found", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@GetMapping(path = "get")
	public ResponseEntity<?> getAllProduct() {
		List<Product> allProduct = productService.getAllProduct();
		if (allProduct.size() == 0) {
			return new ResponseEntity<String>("No Product Found", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Product>>(allProduct, HttpStatus.OK);
	}

	@DeleteMapping(path = "delete/{id}")
	public ResponseEntity<?> deleteProductById(@PathVariable(value = "id") Long id) {
		Product product = productService.getProductById(id);
		if (product == null) {
			return new ResponseEntity<String>("No Product with ID:- " + id + "Found", HttpStatus.BAD_REQUEST);
		}
		String response = productService.deleteProductById(id);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@PatchMapping(path = "/update/{id}")
	public ResponseEntity<?> updateProduct(@Valid @RequestBody String product, @PathVariable(value = "id") Long id)
			throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Product productFromDB = productService.getProductById(id);
		if (productFromDB == null) {
			return new ResponseEntity<String>("No product with id:- " + id, HttpStatus.BAD_REQUEST);
		}
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(product);
		for (Iterator iterator = ((Map<String, String>) obj).keySet().iterator(); iterator.hasNext();) {
			String prop = (String) iterator.next();
			refUtil.getSetterMethod("Product", prop).invoke(productFromDB, obj.get(prop));
		}
		productService.saveProduct(productFromDB);
		return new ResponseEntity<Product>(productFromDB, HttpStatus.OK);
	}
}
