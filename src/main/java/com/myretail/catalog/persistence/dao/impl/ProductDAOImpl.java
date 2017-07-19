package com.myretail.catalog.persistence.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.myretail.catalog.exception.RetailServiceException;
import com.myretail.catalog.helper.RetailConstants;
import com.myretail.catalog.persistence.dao.ProductDAO;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;
import com.myretail.catalog.persistence.repoistory.ProductPriceRepoistory;
import org.springframework.data.mongodb.core.query.Update;

@Repository("productDAO")
public class ProductDAOImpl implements ProductDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDAOImpl.class);

	@Autowired
	@Qualifier("ProductPriceRepoistory")
	private ProductPriceRepoistory prodPriceRepo;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	@Cacheable(value = "products", key = "#productId")

	public ProductPriceEntity getProductById(String productId) {
		LOGGER.info("Fetching product price for {}", productId);
		return prodPriceRepo.findByProductId(productId);

	}

	@Override

	public ProductPriceEntity saveOrUpdate(ProductPriceEntity entity) {
		prodPriceRepo.save(entity);
		return entity;
	}

	@Override
	public void create(ProductPriceEntity entity) {
		prodPriceRepo.insert(entity);
	}

	@Override
	public void delete() {
		prodPriceRepo.deleteAll();
	}

	@Override
	@CachePut(value = "products", key = "#entity.productId")
	public ProductPriceEntity update(ProductPriceEntity entity) {
		Query query = new Query(Criteria.where("productId").is(entity.getProductId()));
		Update update = new Update();
		update.set("price", entity.getPrice());

		WriteResult result = mongoTemplate.updateFirst(query, update, ProductPriceEntity.class);

		int noOfUpdatedRows = result.getN();

		LOGGER.info("Number of UPDATED ROWS for Product id {} ::: {}", entity.getProductId(), noOfUpdatedRows);
		if (noOfUpdatedRows == 0) {
			// If no rows updated, it is an error as only the product id passed does not
			// exist in DB
			throw new RetailServiceException(RetailConstants.PRODUCT_OBJ_NOT_FOUND_EXCEPTION.getCode(),
					RetailConstants.PRODUCT_OBJ_NOT_FOUND_EXCEPTION.getDescription(),
					RetailConstants.PRODUCT_OBJ_NOT_FOUND_EXCEPTION.getDescription());

		}

		return entity;
	}
}
