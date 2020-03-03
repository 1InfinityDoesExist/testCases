package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.example.demo.beans.Customer;
import com.example.demo.beans.CustomerProduct;
import com.example.demo.beans.ManufacturingUnit;
import com.example.demo.beans.Product;
import com.example.demo.repository.CustomerProductRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ManufactureRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.util.ReflectionUtil;

@RestController
@RequestMapping(path = "/object/customer/")
@CrossOrigin
public class CustomerRestController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerProductRepository customerProductRepository;
	@Autowired
	private CustomerProductRepository customerProductRepo;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ManufactureRestController manufactureController;
	@Autowired
	private ManufactureRepository manufacutreRepository;
	@Autowired
	private CustomerProductRestController cpController;

	ReflectionUtil refUtil = ReflectionUtil.getInstance();

	@SuppressWarnings("unused")
	@PostMapping(path = "create")
	public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
		if (result.hasErrors()) {
			// Catch Error And Display In Response
			Map<String, String> error = new HashMap<String, String>();
			for (FieldError fieldError : result.getFieldErrors()) {
				error.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(error, HttpStatus.BAD_REQUEST);
		}
		customer.setBillingDate(LocalDate.now());

		// For Duplicate Email Id CheckPoint...!!!
		Customer customerHistory = customerRepository.findCusotmerByEmailId(customer.getEmail());
		if (customerHistory != null) {
			return new ResponseEntity<String>("Sorry Please Change your email id...dont use it again...",
					HttpStatus.OK);
		}
		customer.setNotes("Thanks For Visititng");
		Customer c = customerService.saveCustoreDetails(customer);
		// Mapping Part of Customer With Product.... ex Patel buys Pulsar86
		Long customerId = c.getId();
		Set<Product> p = customer.getProduct();
		for (Product pp : p) {
			// Long productId = ptr.getId();
			// productRepository.updateJoinBetweenProductAndCustomer(productId, customerId);
			// Creating CustomerProudct Table...!!!

			Product ptr = productRepository.getProductByName(pp.getProductName());// Get Product Specification...!!!
			CustomerProduct customerProduct = new CustomerProduct();// CustomerProduct Relation...!!!
			customerProduct.setEmailId(c.getEmail());
			customerProduct.setCustomerId(c.getId());
			customerProduct.setProductName(ptr.getProductName());
			customerProduct.setBillingDate(c.getBillingDate());
			customerProduct.setProductQuantity(pp.getAvailableProductCount()); // Number of quantity of one particular
			// item...!!!
			customerProduct.setCustomerName(c.getCustomerName());
// customer might not get the product still we keep track of the person....
			// Product product = productRepository.getProductByName(ptr.getProductName());
			// // extra....

			// In case the product does't exist at all... and you want manufacturer to
			// manufacture it for you.... Extreme case....
			if (ptr == null) {
				ManufacturingUnit mu = new ManufacturingUnit();
				mu.setProductName(ptr.getProductName()); // if order placed after 5 days don't add 5 or else add 5
				manufactureController.saveManufacture(mu, null);
				customerRepository.updateNotes(c.getEmail(),
						"This Product Is Not Available. We Have Placed Your Order For Manufacture");
				continue;
			}

			Long productQuantityAvailable = ptr.getAvailableProductCount();// productQuantity available in the
			// store...!!!
			Long proudctQuantityNeeded = pp.getAvailableProductCount();// productQunatity the customer wants...!!!

			if (proudctQuantityNeeded > 0 && proudctQuantityNeeded <= productQuantityAvailable) {
				customerProduct.setProductDeliveryDate(LocalDate.now().plusDays(2)); // Will be delivered with in next
				// two days...!!!
				customerProduct.setDeliveryStatus("SUCCESS");
				cpController.createCusPro(customerProduct, null);// store record of customer and product
				Long remainingProduct = productQuantityAvailable - proudctQuantityNeeded;
				productRepository.updateProductInInventoryTable(remainingProduct, ptr.getProductName());
				// productRepository.updateCustomerIdInventoryTable(ptr.getProductName(),
				// c.getId());
				if (remainingProduct < 10 && remainingProduct >= 0) {
					// if order has already been placed...then don't place it again....if product
					// Quantity is < 10
					ManufacturingUnit history = manufacutreRepository.findProductHistoryByName(ptr.getProductName());
					if (history != null && ptr != null) {
						if (ptr.getManufacturedDate().isEqual(history.getManufacturedEndDate())) {
							ManufacturingUnit mu = new ManufacturingUnit();
							mu.setProductName(ptr.getProductName()); // if order placed after 5 days don't add 5 or else
							// add
							// 5
							manufactureController.saveManufacture(mu, null);
						}
					}

				}
			} else {
				// one more condition...!!!
				if (proudctQuantityNeeded > 0 && proudctQuantityNeeded > productQuantityAvailable) {
					// This is for customer satisfaction that he dont have to wait long...he better
					// look for other shop....
					CustomerProduct pendingCustomer = customerProductRepo.getPendingCustomer(ptr.getProductName(),
							"PENDING");
					if (pendingCustomer != null) {
						Long productQuantity = pendingCustomer.getProductQuantity();
						if (proudctQuantityNeeded > ((20 + productQuantityAvailable) - productQuantity)) {
							return new ResponseEntity<String>("Its Better for you to start looking for another shop",
									HttpStatus.BAD_REQUEST);
						} else {
							customerProduct.setProductDeliveryDate(pendingCustomer.getProductDeliveryDate());
							customerProduct.setDeliveryStatus("PENDING");
						}
						cpController.createCusPro(customerProduct, null);
					} else {
						// product is < 10 but you ordered 15 .... order has already been
						// placed....during product update only....
						ManufacturingUnit history = manufacutreRepository
								.findProductHistoryByName(ptr.getProductName());
						if (history != null && (ptr != null
								&& (!ptr.getManufacturedDate().isEqual(history.getManufacturedEndDate())))) {
							customerProduct.setProductDeliveryDate(history.getAvailibility_date().plusDays(2));
							customerProduct.setDeliveryStatus("PENDING");

						} else {
							// product is > 10 but you order 15...!!!
							ManufacturingUnit mu = new ManufacturingUnit();
							mu.setProductName(ptr.getProductName());
							manufactureController.saveManufacture(mu, null);
							ManufacturingUnit newManufacturingUnit = manufacutreRepository
									.findProductHistoryByName(ptr.getProductName());
							customerProduct
									.setProductDeliveryDate(newManufacturingUnit.getAvailibility_date().plusDays(2)); // Won't
							// be
							// delivered
							// after
							// two
							// days...!!!
							customerProduct.setDeliveryStatus("PENDING");
						}
						cpController.createCusPro(customerProduct, null);
					}
				}
				if (proudctQuantityNeeded < 0) {
					return new ResponseEntity<String>("Quantity Can't Be Negative!!!", HttpStatus.BAD_REQUEST);
				}
			}
			// customerProduct.setProductDeliveryDate(ptr.getAvailibility_date().plusDays(2));
		}
		Map<String, LocalDate> response = new HashMap<String, LocalDate>();
		Set<CustomerProduct> customerProduct = customerProductRepository.getAllCustomerProductDeliveryDate(c.getEmail(),
				c.getBillingDate());
		for (CustomerProduct cp : customerProduct) {
			response.put(cp.getProductName(), cp.getProductDeliveryDate());
		}

		return new ResponseEntity<Map<String, LocalDate>>(response, HttpStatus.CREATED);
	}

	@GetMapping(path = "get/{id}")
	public ResponseEntity<?> getCustomerById(@PathVariable(value = "id") Long id) {
		Customer customer = customerService.getCustomerById(id);
		if (customer == null) {
			return new ResponseEntity<String>("No Customer Found With ID:- " + id, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	@GetMapping(path = "get")
	public ResponseEntity<?> getAllCustomer() {
		List<Customer> allCustomer = customerService.getAllCustomer();
		if (allCustomer.size() == 0) {
			return new ResponseEntity<String>("No Customer Found", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Customer>>(allCustomer, HttpStatus.OK);
	}

	@DeleteMapping(path = "delete/{id}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable(value = "id") Long id) {
		Customer customer = customerService.getCustomerById(id);
		if (customer == null) {
			return new ResponseEntity<String>("No Customer Found with ID:- " + id, HttpStatus.BAD_REQUEST);
		}
		String resonse = customerService.deleteCustomerById(id);
		return new ResponseEntity<String>(resonse, HttpStatus.OK);
	}

	@PatchMapping(path = "update/{id}")
	public ResponseEntity<?> updateCustomerDetails(@Valid @RequestBody String customer,
			@PathVariable(value = "id") Long id)
			throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Customer customerFromDB = customerService.getCustomerById(id);
		if (customerFromDB == null) {
			return new ResponseEntity<String>("No Details Found For This Id:- " + id, HttpStatus.BAD_REQUEST);
		}
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(customer);
		for (Iterator iterator = ((Map<String, String>) obj).keySet().iterator(); iterator.hasNext();) {
			String props = (String) iterator.next();
			refUtil.getSetterMethod("Custormer", props).invoke(customerFromDB, obj.get(props));
		}
		customerService.saveCustoreDetails(customerFromDB);
		return new ResponseEntity<Customer>(customerFromDB, HttpStatus.OK);
	}
}
