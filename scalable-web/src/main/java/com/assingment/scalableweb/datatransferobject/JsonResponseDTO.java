package com.assingment.scalableweb.datatransferobject;

import io.swagger.annotations.ApiModelProperty;

/**
 * Returns the comparison result of {@link JsonDataDO} from {@link DiffController}.
 *
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */

public class JsonResponseDTO {

	@ApiModelProperty(notes="Contains the message", dataType="String")
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
