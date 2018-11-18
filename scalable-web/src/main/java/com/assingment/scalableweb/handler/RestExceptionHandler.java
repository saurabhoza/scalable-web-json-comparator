package com.assingment.scalableweb.handler;

import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.assingment.scalableweb.datatransferobject.error.ErrorDetail;
import com.assingment.scalableweb.exception.MissingJsonDataException;
import com.assingment.scalableweb.exception.ResourceNotFoundException;

/**
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String RESOURCE_NOT_FOUND = "Resource Not Found";
	private static final String MESSAGE_NOT_READABLE = "Message Not Readable";
	private static final String CONSTRAINTS_VALIDATION_FAILED = "Constraints Validation Failed";

	/**
     * Handle ResourceNotFoundException
     * @param rnfe
     * @param request
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {

        logger.debug("ResourceNotFoundException is handled");
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle(RESOURCE_NOT_FOUND);
        errorDetail.setDetails(rnfe.getMessage());

        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle MissingJsonDataException
     * @param mbde
     * @param request
     * @return
     */
    @ExceptionHandler(MissingJsonDataException.class)
    public ResponseEntity<?> handleMissingBinaryDataException(MissingJsonDataException mbde, HttpServletRequest request) {
        logger.debug("MissingJsonDataException is handled");
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle("Missing Data");
        errorDetail.setDetails(mbde.getMessage());

        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }




    /**
     * Handle IllegalArgumentException
     * @param ile
     * @param request
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ile, HttpServletRequest request) {
        logger.debug("IllegalArgumentException is handled");
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle("Binary Data Missing");
        errorDetail.setDetails(ile.getMessage());

        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle MethodArgumentNotValidException
     * @param manve
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.debug("MethodArgumentNotValidException is handled");
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle(CONSTRAINTS_VALIDATION_FAILED);
        errorDetail.setDetails(getErrorMessagesFromException(manve));

        return handleExceptionInternal(manve, errorDetail, headers, status, request);
    }

    private String getErrorMessagesFromException(MethodArgumentNotValidException manve) {
		return manve.getBindingResult().getAllErrors().stream()
				.map(error -> error.getDefaultMessage())
				.collect(Collectors.joining(", "));
	}

	/**
     * Handle HttpMessageNotReadableException
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        logger.debug("HttpMessageNotReadableException is handled");
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle(MESSAGE_NOT_READABLE);
        errorDetail.setDetails(ex.getMessage());
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

}
