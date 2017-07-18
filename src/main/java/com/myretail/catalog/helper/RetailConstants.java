package com.myretail.catalog.helper;

public enum RetailConstants {

	DEFAULT_EXCEPTION ("CERR-1000","Retail service Default Exception"),
	JSON_DESERIALIZE_EXCEPTION("CERR-1001", "Unable to deserialize JSON object"),
	WEB_SERVICE_EXCEPTION("WERR-5000", "Web service Exception "),
	PRODUCT_ID_VALIDATION_EXCEPTION("RSVC-4000", "Invalid Product ID"),
	PRODUCT_NAME_NOT_FOUND_EXCEPTION("RSVC-4001", "Unable to find Product Name"),
	PRODUCT_PRICE_NOT_FOUND_EXCEPTION("RSVC-4002", "Unable to find Product Price"),
	PRODUCT_PRICE_VALIDATION_EXCEPTION("RSVC-4003", "Invalid Product Price "),
	PRODUCT_OBJ_NOT_FOUND_EXCEPTION("RSVC-4004", "Product Information does not exist"),
	PRODUCT_UPDATE_SUCCESSFUL("RSVC-2000", "Product details updated successfully");
	
	private final String code;
	private final String description;
	
	private RetailConstants(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
	
	
}
