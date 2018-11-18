package com.assingment.scalableweb.handler;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Handles the Exception and converts to {@code ErrorDetail} format.
 * 
 * This is a convenient base class for
 * {@link ControllerAdvice @ControllerAdvice} classes that provides a
 * centralized exception handling across all {@code @RequestMapping} methods
 * through {@code @ExceptionHandler} methods and coverts them into
 * {@code ErrorDetail} format.
 *
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String RESOURCE_NOT_FOUND = "Resource Not Found";
	private static final String MESSAGE_NOT_READABLE = "Message Not Readable";
	private static final String CONSTRAINTS_VALIDATION_FAILED = "Constraints Validation Failed";

	/**
	 * Customize the {@code ResourceNotFoundException} response into
	 * {@code ErrorDetail}
	 * 
	 * @param rnfe
	 *            the exception
	 * @param request
	 *            the current request
	 * @return @return a {@code ResponseEntity} instance with
	 *         {@code ErrorDetail} and {@code HttpStatus}
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetail> handleResourceNotFoundException(ResourceNotFoundException rnfe,
			HttpServletRequest request) {

		logger.error("ResourceNotFoundException is handled with error cause : {}", rnfe.getCause());
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
		errorDetail.setTitle(RESOURCE_NOT_FOUND);
		errorDetail.setDetails(rnfe.getMessage());

		return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
	}

	/**
	 * Customize the {@code MissingJsonDataException} response into
	 * {@code ErrorDetail}
	 * 
	 * @param mbde
	 *            the exception
	 * @param request
	 *            the current request
	 * @return a {@code ResponseEntity} instance with {@code ErrorDetail} and
	 *         {@code HttpStatus}
	 */
	@ExceptionHandler(MissingJsonDataException.class)
	public ResponseEntity<ErrorDetail> handleMissingBinaryDataException(MissingJsonDataException mbde,
			HttpServletRequest request) {
		logger.error("MissingJsonDataException is handled with error cause : {}", mbde.getCause());
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
		errorDetail.setTitle("Missing Data");
		errorDetail.setDetails(mbde.getMessage());

		return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
	}

	/**
	 * Customize the {@code MethodArgumentNotValidException} response into
	 * {@code ErrorDetail}
	 *
	 * @param manve
	 *            the exception
	 * @param headers
	 *            the headers to be written to the response
	 * @param status
	 *            the selected response status
	 * @param request
	 *            the current request
	 * @return a {@code ResponseEntity} instance with {@code ErrorDetail} and
	 *         {@code HttpStatus}
	 */
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("MethodArgumentNotValidException is handled with error cause : {}", manve.getCause());
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setTitle(CONSTRAINTS_VALIDATION_FAILED);
		errorDetail.setDetails(getErrorMessagesFromException(manve));

		return handleExceptionInternal(manve, errorDetail, headers, status, request);
	}

	/**
	 * Customize the response for HttpMessageNotReadableException.
	 * 
	 * @param ex
	 *            the exception
	 * @param headers
	 *            the headers to be written to the response
	 * @param status
	 *            the selected response status
	 * @param request
	 *            the current request
	 * @return a {@code ResponseEntity} instance with {@code ErrorDetail} and
	 *         {@code HttpStatus}
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("HttpMessageNotReadableException is handled with error cause : {}", ex.getCause());
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(status.value());
		errorDetail.setTitle(MESSAGE_NOT_READABLE);
		errorDetail.setDetails(ex.getMessage());
		return handleExceptionInternal(ex, errorDetail, headers, status, request);
	}

	private String getErrorMessagesFromException(MethodArgumentNotValidException manve) {
		return manve.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage())
				.collect(Collectors.joining(", "));
	}
}
