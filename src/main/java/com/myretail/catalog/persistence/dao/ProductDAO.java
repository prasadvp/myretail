package com.myretail.catalog.persistence.dao;

import com.myretail.catalog.persistence.entity.ProductPriceEntity;

public interface ProductDAO {
	
	ProductPriceEntity getProductById(String productId);
	
	ProductPriceEntity saveOrUpdate(ProductPriceEntity entity) ;
	
	void create(ProductPriceEntity entity);
	
	void delete();

}
