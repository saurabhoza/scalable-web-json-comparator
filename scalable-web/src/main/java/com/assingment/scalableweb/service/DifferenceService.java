package com.assingment.scalableweb.service;

import java.util.Arrays;
import java.util.StringJoiner;

import javax.xml.bind.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assingment.scalableweb.datatransferobject.JsonResponseDTO;
import com.assingment.scalableweb.domainobject.JsonDataDO;
import com.assingment.scalableweb.enums.Side;
import com.assingment.scalableweb.exception.MissingJsonDataException;
import com.assingment.scalableweb.exception.ResourceNotFoundException;
import com.assingment.scalableweb.repository.JsonDataRepository;

@Service
public class DifferenceService {

	public static final String DIFFERENT_OFFSET = "Left and Right side have same size, but their offsets are different at index : %s";

	public static final String EQUAL_JSON_WITH_DIFF_SIZE = "Left and Right side of Json data does not have same size.";

	public static final String EQUAL_JSON = "Left and Right side of Json data is equal.";

	@Autowired
	public JsonDataRepository repository;

	private static final Logger LOG = LoggerFactory.getLogger(DifferenceService.class);

	/**
	 * 
	 * Save the JSON base64 data into database
	 *
	 * @param id
	 *            unique identifier of the JsonDat
	 * @param data
	 *            in JSON Base64 format
	 * @param side
	 *            holds the left or right side
	 * @return the JsonDataDO stored into the database
	 * @throws Exception
	 *             JsonData
	 */
	public JsonDataDO save(Long id, String data, Side side) {
		JsonDataDO document = null;
		document = repository.findById(id).orElse(new JsonDataDO(id));
		if (Side.LEFT.equals(side)) {
			document.setLeft(data);
		} else if (Side.RIGHT.equals(side)) {
			document.setRight(data);
		} else {
			LOG.warn("Invalid side sent.");
		}
		document = repository.save(document);
		return document;
	}

	/**
	 * Do the core validation in order to compare Jsons and return its results
	 * 
	 * @param id
	 *            is used by repository to find a object
	 * @return a string with comparison results
	 */
	public JsonResponseDTO getDifference(Long id) {
		LOG.debug("Entering getDifference(id={})", id);
		JsonResponseDTO response = new JsonResponseDTO();
		JsonDataDO jsonData = repository.findById(id)
				.orElseThrow(() -> ResourceNotFoundException.build(id));

		LOG.debug("JsonData found. Will check the base64 data on both sides for id '{}'", id);
		
		if (StringUtils.isEmpty(jsonData.getLeft()) || StringUtils.isEmpty(jsonData.getRight())) {
			Side side = StringUtils.isEmpty(jsonData.getLeft()) ? Side.LEFT : Side.RIGHT;
			throw MissingJsonDataException.build(id, side);
		}
		
		byte[] bytesLeft = jsonData.getLeft().getBytes();
		byte[] bytesRight = jsonData.getRight().getBytes();

		boolean blnResult = Arrays.equals(bytesLeft, bytesRight);

		if (blnResult) {
			response.setMessage(EQUAL_JSON);
		} else if (bytesLeft.length != bytesRight.length) {
			response.setMessage(EQUAL_JSON_WITH_DIFF_SIZE);
		} else {
			StringJoiner offsets = new StringJoiner(" ");
			byte different = 0;
			for (Integer index = 0; index < bytesLeft.length; index++) {
				different = (byte) (bytesLeft[index] ^ bytesRight[index]);
				if (different != 0) {
					offsets.add(index.toString());
				}
			}
			response.setMessage(String.format(DIFFERENT_OFFSET, offsets.toString()));
		}
		return response;
	}
}
