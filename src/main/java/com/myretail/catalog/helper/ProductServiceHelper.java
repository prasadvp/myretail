package com.myretail.catalog.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.myretail.catalog.persistence.entity.ProductPriceEntity;
import com.myretail.catalog.persistence.repoistory.ProductPriceRepoistory;

@Component
public class ProductServiceHelper {

	@Autowired
	private ProductPriceRepoistory prodPriceRepo; 
	
	@Cacheable(value ="products", key = "#productId")
	public ProductPriceEntity getProductById(long productId) {
		System.out.println("Fetching product price for "+ productId);
		ProductPriceEntity prodPriceObj = prodPriceRepo.findByProductId(productId);
		
		return prodPriceObj;
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
