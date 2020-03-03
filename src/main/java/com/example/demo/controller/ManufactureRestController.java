package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

import com.example.demo.beans.CustomerProduct;
import com.example.demo.beans.ManufacturingUnit;
import com.example.demo.beans.Product;
import com.example.demo.beans.StageDetails;
import com.example.demo.repository.CustomerProductRepository;
import com.example.demo.repository.ManufactureRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ManufactureService;
import com.example.demo.util.ReflectionUtil;

@RestController
@RequestMapping(path = "/object/manuUnit/")
@CrossOrigin
public class ManufactureRestController {

	@Autowired
	private ManufactureService manufactureService;
	@Autowired
	private ManufactureRepository manufacutreRepository;
	@Autowired
	private ProductRestController productController;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerProductRepository customerProductRepo;

	ReflectionUtil refUtil = ReflectionUtil.getInstance();

	@SuppressWarnings("unused")
	@PostMapping(path = "create")
	public ResponseEntity<?> saveManufacture(@Valid @RequestBody ManufacturingUnit manuUnit, BindingResult result) {
		if (result != null && result.hasErrors()) {
			Map<String, String> error = new HashMap<String, String>();
			for (FieldError fieldError : result.getFieldErrors()) {
				error.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(error, HttpStatus.BAD_REQUEST);
		}

		ManufacturingUnit previousProduct = manufacutreRepository.findProductHistoryByName(manuUnit.getProductName());

		if (previousProduct == null) { // For new Product...!!!
			StageDetails stage = manuUnit.getStage();
			manuUnit.setBatchNumber(1l);
			if (stage == null) { // if stage is not available...then use default stage...
				stage = new StageDetails();
				stage.setStageFirst(1l);
				stage.setStageSecond(3l);
				stage.setStageThird(4l);
				stage.setStageFouth(4l);
			}
			manuUnit.setStage(stage);
			LocalDate today = LocalDate.now();
			manuUnit.setManufacturingStartingDate(today);
			manuUnit.setProductCount(20l);
			manuUnit.setManufacturedEndDate(today.plusDays(
					stage.getStageFirst() + stage.getStageSecond() + stage.getStageThird() + stage.getStageFouth()));
			manuUnit.setAvailibility_date(today.plusDays(
					stage.getStageFirst() + stage.getStageSecond() + stage.getStageThird() + stage.getStageFouth()));
			manuUnit.setDeleteFlag(false);
		} else {
			manufacutreRepository.updateDeleteFlag(previousProduct.getProductName(),
					previousProduct.getManufacturedEndDate());
			manuUnit.setBatchNumber(previousProduct.getBatchNumber() + 1);
			StageDetails stage = new StageDetails();
			stage.setStageFirst(previousProduct.getStage().getStageFirst());
			stage.setStageSecond(previousProduct.getStage().getStageSecond());
			stage.setStageThird(previousProduct.getStage().getStageThird());
			stage.setStageFouth(previousProduct.getStage().getStageFouth());
			manuUnit.setStage(stage);
			LocalDate date = previousProduct.getManufacturedEndDate();

			// After 5 days only new production will start...
			Long elapsedDays = ChronoUnit.DAYS.between(date, LocalDate.now());
			LocalDate startDate = null;
			if (elapsedDays >= 5) {
				startDate = LocalDate.now();
			} else {
				startDate = date.plusDays(5);
			}

			manuUnit.setManufacturingStartingDate(startDate);
			manuUnit.setProductCount(20l);
			Long daysToManufactureProductX = previousProduct.getStage().getStageFirst()
					+ previousProduct.getStage().getStageSecond() + previousProduct.getStage().getStageThird()
					+ previousProduct.getStage().getStageFouth();
			manuUnit.setManufacturedEndDate(startDate.plusDays(daysToManufactureProductX));
			manuUnit.setAvailibility_date(startDate.plusDays(daysToManufactureProductX));
			manuUnit.setDeleteFlag(false);
		}
		ManufacturingUnit manu = manufactureService.saveManufactureData(manuUnit);

		return new ResponseEntity<ManufacturingUnit>(manu, HttpStatus.CREATED);
	}

	@GetMapping(path = "get/{id}")
	public ResponseEntity<?> getManufatureById(@PathVariable(value = "id") Long id) {
		ManufacturingUnit manu = manufactureService.getManufacturingUnitById(id);
		if (manu == null) {
			return new ResponseEntity<String>("Nothing Found on this id:- " + id, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ManufacturingUnit>(manu, HttpStatus.OK);
	}

	@GetMapping(path = "get")
	public ResponseEntity<?> getAllManufacturingUnit() {
		List<ManufacturingUnit> manuUnitList = manufactureService.getAllManuUnit();
		if (manuUnitList.size() == 0) {
			return new ResponseEntity<String>("Sorry No ManufacturingUnit Available", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<ManufacturingUnit>>(manuUnitList, HttpStatus.OK);
	}

	@DeleteMapping(path = "delete/{id}")
	public ResponseEntity<?> deleteManufacturingById(@PathVariable(value = "id") Long id) {
		ManufacturingUnit manu = manufactureService.getManufacturingUnitById(id);
		if (manu == null) {
			return new ResponseEntity<String>("No ManufacturingUnit with id:- " + id, HttpStatus.BAD_REQUEST);
		}
		String response = manufactureService.deleteManufaturingUnit(id);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@PatchMapping(path = "update/{id}")
	public ResponseEntity<?> updateManufacturingUnit(@Valid @RequestBody String manu,
			@PathVariable(value = "id") Long id)
			throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ManufacturingUnit manuFromDB = manufactureService.getManufacturingUnitById(id);

		boolean availibilityFlag = manu.contains("availibility_date");
		boolean manufactureStartDate = manu.contains("manufacturingStartingDate");
		boolean manufactureEndDate = manu.contains("manufacturedEndDate");
		if (manuFromDB == null) {
			return new ResponseEntity<String>("No manufacturingUnit found with id" + id, HttpStatus.BAD_REQUEST);
		}
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(manu);
		for (Iterator iterator = ((Map<String, String>) obj).keySet().iterator(); iterator.hasNext();) {
			String propName = (String) iterator.next();
			if (availibilityFlag || manufactureStartDate || manufactureEndDate) {
				continue;
			} else {
				refUtil.getSetterMethod("ManufacturingUnit", propName).invoke(manuFromDB, obj.get(propName));
			}
			if (availibilityFlag == true) {
				String str = (String) obj.get("availibilityFlag");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate dateTime = LocalDate.parse(str, formatter);
				manuFromDB.setAvailibility_date(dateTime);
			}
			if (manufactureStartDate == true) {
				String str = (String) obj.get("manufactureStartDate");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate dateTime = LocalDate.parse(str, formatter);
				manuFromDB.setManufacturingStartingDate(dateTime);
			}
			if (manufactureEndDate == true) {
				String str = (String) obj.get("manufactureEndDate");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate dateTime = LocalDate.parse(str, formatter);
				manuFromDB.setManufacturedEndDate(dateTime);
			}
		}
		manufactureService.saveManufactureData(manuFromDB);
		return new ResponseEntity<ManufacturingUnit>(manuFromDB, HttpStatus.OK);
	}

	@GetMapping(path = "/async")
	public ResponseEntity<?> asyncMethod() {
		LocalDate todaysDate = LocalDate.now();
		Set<ManufacturingUnit> todaysReadyProduct = manufacutreRepository.getAllReadyProductOfToday(todaysDate);
		for (ManufacturingUnit mfu : todaysReadyProduct) {
			String productName = mfu.getProductName();
			Product product = productRepository.getProductByName(productName);

			if (product != null && (product.getManufacturedDate().isEqual(mfu.getManufacturedEndDate()))) { // sysnc
				// already
				// done no
				// need to
				// do it
				// again ...
				continue;
			}

			if (product != null) { // If available in invent
				Long proudctCount = (Long) (product.getAvailableProductCount() + 20);
				System.out.println(proudctCount);
				productRepository.updateProductInInventoryTable(proudctCount, productName);
				// productRepository.udateManufacutreDate(mfu.getManufacturedEndDate(),
				// productName);
				// productRepository.udpateAvailibalityDate(mfu.getManufacturedEndDate(),
				// productName);

				// updating the pending customer...!!!
				Set<CustomerProduct> pendingCustomers = customerProductRepo.findPendingCustomers(productName,
						"PENDING");
				for (CustomerProduct cp : pendingCustomers) {
					if (cp.getProductQuantity() > proudctCount) { // safety...
						break;
					}
					customerProductRepo.updatePendingCustomerStatus("SUCCESS", cp.getEmailId());
					// Product product1 = productRepository.getProductByName(productName); // no
					// need;
					productRepository.updateProductInInventoryTable(proudctCount - cp.getProductQuantity(),
							productName);
				}
			} else {// If that product Not available in the inventory .... register a new product in
				// that inventory
				Product newProduct = new Product();
				newProduct.setProductName(productName);
				newProduct.setAvailableProductCount(mfu.getProductCount());
				newProduct.setManufacturedDate(mfu.getManufacturedEndDate());
				newProduct.setAvailibility_date(mfu.getAvailibility_date());
				productController.saveProduct(newProduct, null);
			}
		}
		return new ResponseEntity<String>("Sync Completed", HttpStatus.OK);
	}
}
