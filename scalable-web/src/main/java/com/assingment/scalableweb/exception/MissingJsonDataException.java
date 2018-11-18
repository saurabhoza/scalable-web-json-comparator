package com.assingment.scalableweb.exception;

import com.assingment.scalableweb.enums.Side;

/**
 * Exception occurs when one or two sides of {@code JsonDataDO} is empty or null 
 * 
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>
 */
public class MissingJsonDataException extends RuntimeException {

    private static final String MISSING_JSON_DATA = "The %s side of Json Data is missing for id : %d.";
	private static final long serialVersionUID = 1L;

    public MissingJsonDataException() {
    }

    public MissingJsonDataException(String message) {
        super(message);
    }

    public static MissingJsonDataException build(Long id, Side side) {
        return new MissingJsonDataException(getMissingJsonDataMessage(id, side));
    }

	public static String getMissingJsonDataMessage(Long id, Side side) {
		return String.format(MISSING_JSON_DATA, side.toString(), id);
	}
}