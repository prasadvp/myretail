package com.myretail.catalog.persistence.entity;


import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "productPrice")
public class ProductPriceEntity {

	
	@Id
	private String id;
	
	private long productId;
	
	private BigDecimal price;
	
	private String currencyCode;

	private String updatedBy;
	
	private Date updatedTime;

	
	
	public ProductPriceEntity(long productId, BigDecimal price, String currencyCode) {
		
		this.productId = productId;
		this.price = price;
		this.currencyCode = currencyCode;
	}

	

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}
	
	
}
