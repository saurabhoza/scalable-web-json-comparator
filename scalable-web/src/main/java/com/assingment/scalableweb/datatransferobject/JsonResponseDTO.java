package com.assingment.scalableweb.datatransferobject;

public class JsonResponseDTO {

	private String message;

	public String getMessage() {
		return message;
	}

	public JsonResponseDTO() {
		super();
	}

	public JsonResponseDTO(String message) {
		super();
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
