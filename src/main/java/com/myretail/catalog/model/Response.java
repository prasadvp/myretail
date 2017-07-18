package com.myretail.catalog.model;

import java.util.ArrayList;
import java.util.List;

public class Response {
	
	private String status;
	
	private List<Message> messages;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Message> getMessages() {
		if(messages == null) {
			messages = new ArrayList<>();
		}
		return messages;
	}

}
