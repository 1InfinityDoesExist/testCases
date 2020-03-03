package com.example.demo.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.demo.FirstProApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FirstProApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {
	@LocalServerPort
	protected int port;

	@Autowired
	protected ObjectMapper objectMapper = new ObjectMapper();
	protected HttpHeaders header = new HttpHeaders();
	protected TestRestTemplate restTemplate;

	protected String fullURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
