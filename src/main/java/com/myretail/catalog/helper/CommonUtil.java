package com.myretail.catalog.helper;

import com.myretail.catalog.model.Message;
import com.myretail.catalog.model.Response;
import com.myretail.catalog.model.Status;

public class CommonUtil {

	public static boolean isValidProductId(long id) {
		boolean result = true;
		if(id <= 0) {
			result = false;
		}
		return result;
	}
	
	public static Response buildResponse (Response obj, Status status, String message, String code) {
		obj.setStatus(status.value());
		Message msg = new Message();
		msg.setCode(code);
		msg.setMessage(message);
		obj.getMessages().add(msg);
		
		return obj;
	}
}
