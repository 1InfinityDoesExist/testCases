package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.CustomerProduct;
import com.example.demo.service.CustomerProductService;
import com.example.demo.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin
@RequestMapping(path = "/object/cusotmerProduct/")
@Api(value = "CustomerProduct Controller", description = "CRUD Operation of CustomerProducetRest Controller")
public class CustomerProductRestController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerProductRestController.class);
	@Autowired
	private CustomerProductService cusService;

	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void setUp() {
		objectMapper.registerModule(new JavaTimeModule());
	}

	ReflectionUtil refUtil = ReflectionUtil.getInstance();

	@RequestMapping(path = "create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED, reason = "SuccessFully Persisted Instance In The Database")
	@ApiOperation(value = "Create CusotmerProduct Instace In The Database", notes = "Just Create CustomerProduct Instance In The Database", response = CustomerProduct.class)
	public ResponseEntity<?> createCusPro(@Valid @RequestBody CustomerProduct customerProduct, BindingResult result) {
		logger.info("***************************Customer Product***********************************");
		if (result != null && result.hasErrors()) {
			Map<String, String> error = new HashMap<String, String>();
			for (FieldError fieldError : result.getFieldErrors()) {
				error.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(error, HttpStatus.BAD_REQUEST);
		}
		CustomerProduct cp = cusService.createCusotmerProduct(customerProduct);
		logger.info("CustomerProduct:- " + cp);
		return new ResponseEntity<CustomerProduct>(cp, HttpStatus.CREATED);
	}

	@RequestMapping(path = "get/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK, reason = "Successfully Retrieved CustomeProduct Data From The DB")
	@ApiOperation(value = "Get CustomerProduct By ID", notes = "Retrieve Data of CusomerProudct By ID", response = CustomerProduct.class)
	public ResponseEntity<?> getCustomerProduct(
			@ApiParam(value = "id", required = true, example = "123") @PathVariable(value = "id", required = true) Long id) {
		logger.info("******CustomerProduct Get Method************");
		CustomerProduct cp = cusService.getCustomerProductById(id);
		if (cp == null) {
			return new ResponseEntity<String>("Nothing Found with id:- " + id, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<CustomerProduct>(cp, HttpStatus.OK);
	}

	@RequestMapping(path = "get", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK, reason = "SuccessFully Retieved All CustomerProduct From The DB")
	@ApiOperation(value = "Retrieve All CustomerProduct", notes = "Get All The CustomerProduct ", response = CustomerProduct.class, responseContainer = "LIST")
	public ResponseEntity<?> getAllCusotmerProduct() {
		logger.info("************Inside Customer Get Method**********");
		List<CustomerProduct> cpList = cusService.getCustomerProductAll();
		if (cpList.size() == 0) {
			return new ResponseEntity<String>("Nothing Found ", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<CustomerProduct>>(cpList, HttpStatus.OK);
	}

	@RequestMapping(path = "delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK, reason = "Successfully Deleted CustomerProduct From The DB")
	@ApiOperation(value = "Remove Resource From The DB", notes = "Successfully Removed Resources From The DB", response = String.class)
	public ResponseEntity<?> deleteCustomerProductById(
			@ApiParam(value = "id", required = true, example = "123") @PathVariable(value = "id") Long id) {
		logger.info("**************CustomerProduct delete Method********");
		CustomerProduct cp = cusService.getCustomerProductById(id);
		if (cp == null) {
			return new ResponseEntity<String>("No CustomerProduct found for id:-" + id, HttpStatus.BAD_REQUEST);
		}
		cusService.deleteCustoProduct(id);
		return new ResponseEntity<String>("SuccessFully Deleted The CustomerProduct", HttpStatus.OK);

	}

	@RequestMapping(path = "update/{id}", method = RequestMethod.PATCH, produces = "application/json")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK, reason = "Updating Resource For CustomerProduct")
	@ApiOperation(value = "Update Resource For CustomerProduct", notes = "Updated Resources for CustomerProduct", response = CustomerProduct.class)
	public ResponseEntity<?> updateCustomerProduct(@Valid @RequestBody String cp,
			@ApiParam(value = "id", required = true, example = "123") @PathVariable(value = "id") Long id)
			throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		logger.info("**********************Inside update Method of CusotmerProduct*********************");
		CustomerProduct cpFromDB = cusService.getCustomerProductById(id);
		boolean deliveryFlag = cp.contains("productDeliveryDate");
		if (cpFromDB == null) {
			return new ResponseEntity<String>("No CustomerProduct with id:- " + id + "Found", HttpStatus.BAD_REQUEST);
		}
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(cp);

		for (Iterator iterator = ((Map<String, String>) obj).keySet().iterator(); iterator.hasNext();) {
			String props = (String) iterator.next();
			if (!props.equals("productDeliveryDate")) {
				refUtil.getSetterMethod("CustomerProduct", props).invoke(cpFromDB, obj.get(props));
			}
		}
		if (deliveryFlag == true) {
			String str = (String) obj.get("productDeliveryDate");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			LocalDate dateTime = LocalDate.parse(str, formatter);

			cpFromDB.setProductDeliveryDate(dateTime);
		}
		CustomerProduct cpFromdb = cusService.createCusotmerProduct(cpFromDB);
		return new ResponseEntity<CustomerProduct>(cpFromdb, HttpStatus.OK);
	}
}
