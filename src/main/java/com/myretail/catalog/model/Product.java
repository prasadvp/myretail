package com.myretail.catalog.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.myretail.catalog.helper.ProductDetailsDeserializer;


@JsonDeserialize(using = ProductDetailsDeserializer.class)
public class Product {
	@JsonProperty("product_id")
	private long id;
	
	@JsonProperty("title")
	private String name;
	
	private Price current_price;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Price getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(Price current_price) {
		this.current_price = current_price;
	}

}
