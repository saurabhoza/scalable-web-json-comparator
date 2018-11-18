package com.assingment.scalableweb.datatransferobject;

/**
 * Returns the comparison result of {@code JsonDataDO} from {@code DiffController}.
 *
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */

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
