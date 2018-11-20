package com.assingment.scalableweb.service;

import java.util.Arrays;
import java.util.StringJoiner;

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

/**
 * Handles all the business layer logic of storing, updating and comparing the
 * {@link JsonDataDO} entity.
 * 
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.ss
 */
@Service
public class DifferenceService {

	public static final String DIFFERENT_OFFSET = "Left and Right side have same size, but their offsets are different at index : %s";
	public static final String EQUAL_JSON_WITH_DIFFERENT_SIZE = "Left and Right side of Json data does not have same size.";
	public static final String EQUAL_JSON_SUCCESS_MESSAGE = "Left and Right side of Json data is equal.";

	@Autowired
	public JsonDataRepository repository;

	private static final Logger LOGGER = LoggerFactory.getLogger(DifferenceService.class);

	/**
	 * 
	 * Saves the JSON base64 {@link JsonDataDO} data into repository
	 *
	 * @param id
	 *            Unique identifier of {@link JsonDataDO}
	 * @param data
	 *            in JSON Base64 format
	 * @param side
	 *            holds the left or right side of {@link Side}
	 * @return {@link JsonDataDO} stored into the repository
	 */
	public JsonDataDO save(Long id, String data, Side side) {
		LOGGER.debug("Entered in save(id={}, data={}, side={})", id, data, side);
		JsonDataDO jsonData = repository.findById(id).orElse(new JsonDataDO(id));
		if (Side.LEFT == side) {
			jsonData.setLeft(data);
		} else if (Side.RIGHT == side) {
			jsonData.setRight(data);
		}
		jsonData = repository.save(jsonData);
		LOGGER.debug("Exiting from save with saved object : {} ", jsonData);
		return jsonData;
	}

	/**
	 * Compares the left and right sides of {@link JsonDataDO} Jsons and return
	 * its results as {@link JsonResponseDTO}
	 * 
	 * @param id
	 *            Unique identifier of {@link JsonDataDO}
	 * @return a difference information in {@link JsonResponseDTO}
	 */
	public JsonResponseDTO getDifference(Long id) {
		LOGGER.debug("Entered in getDifference(id={})", id);
		JsonResponseDTO response = new JsonResponseDTO();
		JsonDataDO jsonData = repository.findById(id).orElseThrow(() -> ResourceNotFoundException.build(id));

		LOGGER.debug("JsonData found. Will check the base64 data on both sides for id '{}'", id);

		if (StringUtils.isEmpty(jsonData.getLeft()) || StringUtils.isEmpty(jsonData.getRight())) {
			Side side = StringUtils.isEmpty(jsonData.getLeft()) ? Side.LEFT : Side.RIGHT;
			throw MissingJsonDataException.build(id, side);
		}

		byte[] bytesLeft = jsonData.getLeft().getBytes();
		byte[] bytesRight = jsonData.getRight().getBytes();

		boolean blnResult = Arrays.equals(bytesLeft, bytesRight);

		if (blnResult) {
			response.setMessage(EQUAL_JSON_SUCCESS_MESSAGE);
		} else if (bytesLeft.length != bytesRight.length) {
			response.setMessage(EQUAL_JSON_WITH_DIFFERENT_SIZE);
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
