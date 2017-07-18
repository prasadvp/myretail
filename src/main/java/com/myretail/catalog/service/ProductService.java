package com.myretail.catalog.service;


import com.myretail.catalog.model.Product;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;

public interface ProductService {
	

	/**
	 * This method will retrieve the product using product id. It invokes the 
	 * async method to fetch the product name and price information in parallel
	 * It will throw RetailServiceException
	 * @param productId
	 * @return
	 */
	Product getProductDetail(long productId);
	/**
	 * This method will modify the price information of product 
	 * 
	 * @param product
	 */
	void modifyProduct(Product product);
	/**
	 * This method will delete the product information from database. It is used
	 * only for testing / demo purpose
	 */
	void delete();
	/**
	 * This method will update or create the product price information
	 *  in the database 
	 * @param entity
	 */
	void save(ProductPriceEntity entity);

}
