package com.assingment.scalableweb.controller;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assingment.scalableweb.datatransferobject.JsonRequestDTO;
import com.assingment.scalableweb.datatransferobject.JsonResponseDTO;
import com.assingment.scalableweb.enums.Side;
import com.assingment.scalableweb.service.DifferenceService;

/**
 * 
 * This controller is responsible for getting the difference of 2 Base64 encoded
 * Jsons
 *
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */
@RestController
@RequestMapping("/v1/diff/{id}")
public class DifferenceController {

	private static final String JSON_DATA_SAVED_SUCCESSFULY = "Json Data on the %s side saved successfuly";

	private static final Logger LOGGER = LoggerFactory.getLogger(DifferenceController.class);

	@Autowired
	private DifferenceService service;

	/**
	 * 
	 * Stores the left side in the JSON base64 base String for comparison
	 *
	 * @param id
	 *            unique identifier.
	 * @param request
	 *            string based base64 value to store.
	 * @return an object of JsonResponseDTO with operation message to be showed
	 *         in the response.
	 */
	@PostMapping(value = "/left", produces = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<JsonResponseDTO> createLeftSide(@PathVariable Long id,
			@Valid @RequestBody JsonRequestDTO request) {
		LOGGER.debug("Entering createLeftSide(id={}, request={})", id, request);
		return createJsonData(id, request, Side.LEFT);
	}

	/**
	 * 
	 * Stores the left side in the JSON base64 base String for comparison
	 *
	 * @param id
	 *            unique identifier. If posted some already existing value, it
	 *            will be updated.
	 * @param request
	 *            string based base64 value for being updated.
	 * @return an object of JsonResponseDTO with operation message to be showed in
	 *         the response.
	 */
	@PostMapping(value = "/right", produces = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<JsonResponseDTO> createRightSide(@PathVariable Long id, @Valid @RequestBody JsonRequestDTO request) {
		LOGGER.debug("Entering createRightSide(id={}, request={})", id, request);
		return createJsonData(id, request, Side.RIGHT);
	}

	/**
	 * 
	 * Returns the comparison result between left and right sides of a JSON base64 base String.
	 *
	 * @param id
	 *            JsonData identifier
	 * @return an object of JsonResponseDTO with operation message to be showed in
	 *         the response.
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<JsonResponseDTO> getDifference(@PathVariable Long id) {
		return new ResponseEntity<>(service.getDifference(id), HttpStatus.OK);
	}

	/**
	 * Stores the Left / Right side of the JSON base64 base String into database
	 * 
	 * @param id
	 *            JsonData identifier
	 * @param request string based base64 value for being updated.
	 * @param side contains the side where the jsno data will be stored
	 * @return an object of JsonResponseDTO with simple message to be showed in
	 *         the response.
	 */
	private ResponseEntity<JsonResponseDTO> createJsonData(Long id, JsonRequestDTO request, Side side) {
		LOGGER.debug("Entering createJsonData(id={}, request={}, side={})", id, request, side);
		service.save(id, request.getData(), side);

		LOGGER.info("'{}' side of the createJsonData saved successfuly with id :'{}'", side, id);
		JsonResponseDTO response = new JsonResponseDTO(getSuccessResponse(side));

		LOGGER.debug("Exiting createJsonData with response : {}", response);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	public static String getSuccessResponse(Side side) {
		return String.format(JSON_DATA_SAVED_SUCCESSFULY, side.toString());
	}
}
