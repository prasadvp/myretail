package com.myretail.catalog.persistence.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.myretail.catalog.persistence.dao.ProductDAO;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;
import com.myretail.catalog.persistence.repoistory.ProductPriceRepoistory;

@Repository("productDAO")
public class ProductDAOImpl implements ProductDAO{


	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDAOImpl.class);

	@Autowired
	private ProductPriceRepoistory prodPriceRepo; 
	
	@Cacheable(value ="products", key = "#productId")
	public ProductPriceEntity getProductById(String productId) {
		LOGGER.info("Fetching product price for {}", productId);
		return prodPriceRepo.findByProductId(productId);
		
		
	}
	
	@CachePut(value = "products" , key = "#entity.productId")
	public ProductPriceEntity saveOrUpdate(ProductPriceEntity entity) {
		prodPriceRepo.save(entity);
		return entity;
	}
	
	public void create(ProductPriceEntity entity) {
		prodPriceRepo.insert(entity);
	}
	
	public void delete() {
		prodPriceRepo.deleteAll();
	}
}
