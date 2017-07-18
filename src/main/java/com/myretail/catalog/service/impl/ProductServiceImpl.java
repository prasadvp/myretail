package com.myretail.catalog.service.impl;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myretail.catalog.exception.RetailServiceException;
import com.myretail.catalog.helper.RetailConstants;
import com.myretail.catalog.model.Price;
import com.myretail.catalog.model.Product;
import com.myretail.catalog.persistence.dao.ProductDAO;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;
import com.myretail.catalog.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Autowired
	@Qualifier("productDAO")
	private ProductDAO prodDAO;

	private RestTemplate restTemplate;

	@Value("${products.api.uri}")
	private String productApiURL;

	@Value("${products.api.default.queryparam}")
	private String productApiQueryParam;

	@Cacheable(value = "productnames", key = "#productId")
	@Async
	public CompletableFuture<Product> getProductNameAsyncMode(long productId) {
		LOGGER.info("Fetching product name for {}",productId);
		StringBuilder prodURL = new StringBuilder();
		prodURL.append(productApiURL).append(Long.toString(productId)).append(productApiQueryParam);

		LOGGER.info("URL {}",prodURL);
		Product prod = restTemplate.getForObject(prodURL.toString(), Product.class);
		return CompletableFuture.completedFuture(prod);
	}

	@Async
	public CompletableFuture<Price> getProductPriceAsyncMode(long productId) {

		Price productPrice = null;

		ProductPriceEntity prodEntity = getProductById(productId);

		if (prodEntity != null) {
			productPrice = new Price();
			productPrice.setValue(prodEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
			productPrice.setCurrency_code(prodEntity.getCurrencyCode());
		}

		return CompletableFuture.completedFuture(productPrice);
	}

	private ProductPriceEntity getProductById(long productId) {

		return prodDAO.getProductById(Long.toString(productId));

	}

	@PostConstruct
	public void initializeRestTemplate() {
		restTemplate = new RestTemplateBuilder().build();
	}

	public void modifyProduct(Product product) {

		LOGGER.info("Price to be updated as {} for {}",product.getCurrent_price().getValue() , product.getId());
		
		ProductPriceEntity updatedProd = new ProductPriceEntity(Long.toString(product.getId()),
				product.getCurrent_price().getValue(), product.getCurrent_price().getCurrency_code());
		try {
			save(updatedProd);
		} catch (Exception ex) {
			throw new RetailServiceException(RetailConstants.DEFAULT_EXCEPTION.getCode(),
					RetailConstants.DEFAULT_EXCEPTION.getDescription(), ex.getMessage());
		}

		
	}

	public void delete() {
		prodDAO.delete();
	}

	public void save(ProductPriceEntity entity) {
		prodDAO.saveOrUpdate(entity);
	}

	public Product getProductDetail(long productId) {
		Product product = null;
		try {
			// Parallel execution Start
			CompletableFuture<Product> productAsync = getProductNameAsyncMode(productId);
			CompletableFuture<Price> priceAsync = getProductPriceAsyncMode(productId);
			// Wait until they are all done
			CompletableFuture.allOf(productAsync, priceAsync).join();
			// Parallel execution End
			product = productAsync.get();
			Price currentPrice = priceAsync.get();

			if (product == null) {
				product = new Product();
			}
			product.setCurrent_price(currentPrice);

		} catch (Exception ex) {
			throw new RetailServiceException(RetailConstants.DEFAULT_EXCEPTION.getCode(),
					RetailConstants.DEFAULT_EXCEPTION.getDescription(), ex.getMessage());
		}

		return product;

	}
}
