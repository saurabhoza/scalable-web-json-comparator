package com.assingment.scalableweb.datatransferobject;


import javax.validation.constraints.NotEmpty;

import com.assingment.scalableweb.validation.Base64Validation;

/**
 * Class created for receiving the json data from request on the DiffController.
 *
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */
public class JsonRequestDTO {

	public JsonRequestDTO() {
		super();
	}

	public JsonRequestDTO(String data) {
		super();
		this.data = data;
	}

	/**
	 * Simple string variable that receives the base64 encoded JSON data
	 */
	@NotEmpty(message="Data field must not be empty.")
	@Base64Validation
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}