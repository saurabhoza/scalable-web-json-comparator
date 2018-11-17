package com.assingment.scalableweb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assingment.scalableweb.dto.JSONRequest;
import com.assingment.scalableweb.dto.JSONResponse;

@RestController
@RequestMapping("/v1/diff/{id}")
public class DifferenceController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DifferenceController.class);
	
	@PostMapping(path="left", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> saveLeftJsonData(@PathVariable("id") Long id, @RequestBody JSONRequest request) {
		LOGGER.debug("Entered in saveLeftJsonData with id : {} and request : {}",id, request);
		
		return new ResponseEntity<JSONResponse>(new JSONResponse(), HttpStatus.CREATED);
	}
	
	@PostMapping(path="right", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> saveRightJsonData(@PathVariable("id") Long id, @RequestBody JSONRequest request) {
		LOGGER.debug("Entered in saveRightJsonData with id : {} and request : {}",id, request);
		
		return new ResponseEntity<JSONResponse>(new JSONResponse(), HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<JSONResponse> findDifferenceInBothJson(@PathVariable("id") Long id) {
		LOGGER.debug("Entered in findDifferenceInBothJson with id : {}",id);
		return new ResponseEntity<JSONResponse>(new JSONResponse(), HttpStatus.OK);
	}
}
