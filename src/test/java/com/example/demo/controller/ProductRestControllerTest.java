package com.example.demo.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.beans.Customer;
import com.example.demo.beans.Product;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ProductRestControllerTest extends AbstractTest {
	private final String URIPRODUCT = "/object/product/create";
	private final String URICUSTOMER = "/object/customer/create";
	private Product product;
	private Customer customer;

	@Autowired
	ObjectMapper objectMapper;

	@PostConstruct
	public void init() throws JsonParseException, JsonMappingException, IOException, UnirestException {

		// Product has mapping with Customer so need to make a product first...!!!
		InputStream inCustomer = CustomerRestControllerTest.class.getResourceAsStream("Customer.json");
		customer = objectMapper.readValue(inCustomer, Customer.class);
		HttpResponse<JsonNode> jsonNodeCustomer = Unirest.post(fullURLWithPort(URICUSTOMER))
				.header("Accept", "application/json").header("Content-Type", "application/json").body(inCustomer)
				.asJson();
		JSONObject jsonObjectCustomer = jsonNodeCustomer.getBody().getObject();

		// Product has mapping with Customer
		InputStream inProduct = ProductRestControllerTest.class.getResourceAsStream("Product.json");
		product = objectMapper.readValue(inProduct, Product.class);
		HttpResponse<JsonNode> jsonNodeProduct = Unirest.post(fullURLWithPort(URIPRODUCT))
				.header("Accept", "application/json").header("Content-Type", "application/json").body(inProduct)
				.asJson();
		JSONObject jsonObjectProduct = jsonNodeProduct.getBody().getObject();
		Gson gson = new Gson();

		// In case the customer in product is null...!!!
		if (product.getCustomer() == null) {
			product.setCustomer(new Customer());
		}
		product.setCustomer(objectMapper.readValue((gson.toJson(jsonObjectProduct, Customer.class)), Customer.class));

	}

	@Test
	public void saveProduct() throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.post(fullURLWithPort(URIPRODUCT)).header("Accept", "application/json")
				.header("Content-Type", "application/json").body(product).asJson();
		JSONObject jsonObject = response.getBody().getObject();
		for (Object object : jsonObject.keySet()) {
			String propName = (String) object;
			switch (propName) {
			case "productName":
				Object productNameObject = jsonObject.get(propName);
				String productNameValue = (String) productNameObject;
				assertEquals(productNameValue, product.getProductName());
				continue;
			default:
				continue;

			}
		}

	}

}
