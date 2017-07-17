package com.myretail.catalog.persistence.repoistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myretail.catalog.persistence.entity.ProductPriceEntity;



@Repository
public interface ProductPriceRepoistory extends MongoRepository<ProductPriceEntity, Long> {

	
	 public ProductPriceEntity findByProductId(long productId);
	 
	
}
