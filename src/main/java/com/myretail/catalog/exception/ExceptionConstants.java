package com.myretail.catalog.exception;

public enum ExceptionConstants {

	DEFAULT_EXCEPTION ("CERR-1000","Retail service Default Exception"),
	WEB_SERVICE_EXCEPTION("WERR-2000", "Web service Exception "),
	VALIDATION_EXCEPTION("RSVC-3000", "Invalid Product ID");
	
	private final String errorCode;
	private final String description;
	
	private ExceptionConstants(String errorCode, String description) {
		this.errorCode = errorCode;
		this.description = description;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getDescription() {
		return description;
	}
	
	
	
}
