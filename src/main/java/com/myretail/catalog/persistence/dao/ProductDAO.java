package com.myretail.catalog.persistence.dao;

import com.myretail.catalog.persistence.entity.ProductPriceEntity;

public interface ProductDAO {
	/**
	 * This method will retrieve the product price details from the DB for given Product ID
	 * @param productId. It also uses cache for fast retrieval
	 * @return
	 */
	ProductPriceEntity getProductById(String productId);
	/**
	 * This method will save or update the given product price entity, 
	 * Currently this smethod is used for testing and not called by the client  service 
	 * @param entity
	 * @return
	 */
	ProductPriceEntity saveOrUpdate(ProductPriceEntity entity) ;
	/**
	 * This method will create the products in Database. Currently this method
	 * is used for testing
	 * @param entity
	 */
	void create(ProductPriceEntity entity);
	/**
	 * This method will delete the product details from database 
	 */
	void delete();
	/**
	 * This method will  update for the given product price entity. It also updates the cache if it e
	 * presnt. 
	 * if it exists else it return an exception
	 * @param entity
	 * @return
	 */
	ProductPriceEntity update(ProductPriceEntity entity) ;

}
