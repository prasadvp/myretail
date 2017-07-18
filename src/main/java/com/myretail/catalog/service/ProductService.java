package com.myretail.catalog.service;


import com.myretail.catalog.model.Product;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;

public interface ProductService {
	

	
	Product getProductDetail(long productId);
	
	void modifyProduct(Product product);
	void delete();
	void save(ProductPriceEntity entity);

}
