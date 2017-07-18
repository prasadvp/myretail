package com.myretail.catalog.exception;

public class RetailServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8838473941727726589L;

	private final String errorCode;
	private final String description;
	public RetailServiceException(String errorCode, String description) {
		super();
		this.errorCode = errorCode;
		this.description = description;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public String getDescription() {
		return description;
	}
	
	public RetailServiceException(String errorCode, String description, String message) {
		super(message);
		this.errorCode = errorCode;
		this.description = description;
	}
	
	
}
