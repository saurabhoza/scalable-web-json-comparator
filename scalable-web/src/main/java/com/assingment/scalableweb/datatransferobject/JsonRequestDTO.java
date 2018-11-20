package com.assingment.scalableweb.datatransferobject;

import javax.validation.constraints.NotEmpty;

import com.assingment.scalableweb.validation.Base64Validation;

import io.swagger.annotations.ApiModelProperty;

/**
 * Receives the Base 64 format JSON data from request in {@link DiffController}.
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
	@ApiModelProperty(notes="Contains the Base 64 encoded String", allowableValues="dGVzdGluZyB0aGUgYmFzZTY0", required=true, dataType="String")
	@NotEmpty(message = "Data field must not be empty.")
	@Base64Validation // custom validation to check if the string is in a valid base64 format.
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}