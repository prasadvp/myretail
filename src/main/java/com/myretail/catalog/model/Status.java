package com.myretail.catalog.model;

public enum Status {
	
	SUCCESS,
	ERROR,
	WARNING;
	
	public String value() {
		return name();
	}

	public static Status fromValue(String s) {
		return valueOf(s);
	}
}
