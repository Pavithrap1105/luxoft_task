package com.luxoft.learntoday.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.luxoft.learntoday.exception.ExceptionDetails;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;

@RestControllerAdvice
public class ApplicationExceptionHandler {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidException(MethodArgumentNotValidException exception) {
		Map<String, String> mapError = new HashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			mapError.put(error.getField(), error.getDefaultMessage());
		});
		return mapError;
	}

	@ExceptionHandler(ResourseNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourseNotFoundException exception,
			WebRequest request) {
		ExceptionDetails exceptionDetails = new ExceptionDetails(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
		return new ResponseEntity<ExceptionDetails>(exceptionDetails, exceptionDetails.getStatusCode());
	}
	
	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<ExceptionDetails> handleResourceAlreadyExistException(ResourceAlreadyExistException exception,
			WebRequest request) {
		ExceptionDetails exceptionDetails = new ExceptionDetails(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
		return new ResponseEntity<ExceptionDetails>(exceptionDetails, exceptionDetails.getStatusCode());
	}
	
}
