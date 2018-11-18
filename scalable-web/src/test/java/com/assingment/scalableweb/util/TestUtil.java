package com.assingment.scalableweb.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class contains the util methods of Test
 * 
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 *
 */
public class TestUtil {

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * Converts the {@code Object} into {@code String} JSON format
	 * 
	 * @param object
	 * @return JSON representation of {@code Object}
	 * @throws IOException
	 */
	public static String convertObjectToJsonBytes(Object object) throws IOException {
		return mapper.writeValueAsString(object);
	}
}
