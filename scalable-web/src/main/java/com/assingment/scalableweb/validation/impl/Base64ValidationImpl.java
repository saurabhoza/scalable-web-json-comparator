package com.assingment.scalableweb.validation.impl;

import java.util.Base64;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assingment.scalableweb.validation.Base64Validation;

/**
 * This class validates the String in Base 64 format for annotation
 * {@link Base64Validation}.
 * 
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */
public class Base64ValidationImpl implements ConstraintValidator<Base64Validation, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Base64ValidationImpl.class);

	@Override
	public boolean isValid(String jsonData, ConstraintValidatorContext arg1) {
		boolean isValid = true;

		if (jsonData != null) {
			Base64.Decoder dec = Base64.getDecoder();
			try {
				dec.decode(jsonData);
			} catch (IllegalArgumentException e) {
				isValid = false;
				LOGGER.error("Invalid Base64 Json data paaesed : {}", jsonData);
			}
		}
		return isValid;
	}
}
