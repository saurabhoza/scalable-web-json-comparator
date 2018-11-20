package com.assingment.scalableweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception occurs when the {@link JsonDataDO} data is not found in the repository for id 
 *
 *@author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String RESOURCE_NOT_FOUND= "JsonData with id : %d not found";

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException build(Long id) {
        return new ResourceNotFoundException(getResourceNotFoundMessage(id));

    }

	public static String getResourceNotFoundMessage(Long id) {
		return String.format(RESOURCE_NOT_FOUND, id);
	}
}