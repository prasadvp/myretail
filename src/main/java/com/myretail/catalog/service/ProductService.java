package com.myretail.catalog.service;

import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myretail.catalog.helper.ProductServiceHelper;
import com.myretail.catalog.model.Price;
import com.myretail.catalog.model.Product;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;

@Service
public class ProductService {

	@Autowired
	private ProductServiceHelper prodSvcHelper;
	
	 private  RestTemplate restTemplate;
	 
	 @Value("${products.api.uri}")
	 private String productApiURL ; 
	 
	 @Value("${products.api.default.queryparam}")
	 private String productApiQueryParam;
	
	//TEST METHOD
	public Product getProductName(long productId) {
		
		Product prod = restTemplate.getForObject(
				"http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics",
				Product.class);
		return prod;
	}
	
	

	@Cacheable(value="productnames" , key="#productId")
	@Async
	public CompletableFuture<Product> getProductNameAsyncMode(long productId) {
		System.out.println("Fetching product name for "+ productId);
		StringBuilder prodURL = new StringBuilder();
		prodURL.append(productApiURL).append(Long.toString(productId)).append(productApiQueryParam);
		
		/*Product prod = restTemplate.getForObject(
				"http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics",
				Product.class);*/
		System.out.println("URL" +prodURL );
		Product prod = restTemplate.getForObject(prodURL.toString(), Product.class);
		return CompletableFuture.completedFuture(prod);
	}
	
	@Async
	public CompletableFuture<Price> getProductPriceAsyncMode(long productId) {
		
		Price productPrice = null;
		
		ProductPriceEntity prodEntity = getProductById(productId);
		
		if(prodEntity!=null) {
			productPrice = new Price() ;
			productPrice.setValue(prodEntity.getPrice());
			productPrice.setCurrency_code(prodEntity.getCurrencyCode());
		}
		
		return CompletableFuture.completedFuture(productPrice);
	}

	
	private ProductPriceEntity getProductById(long productId) {
		// TODO Auto-generated method stub
		return prodSvcHelper.getProductById(productId);
		 
	}


	@PostConstruct
	public void initializeRestTemplate() {
		restTemplate = new RestTemplateBuilder().build();
	}
	
	/*
	 * TEST METHOD
	 */
	public Price getProductPrice(long productId) {
		
		Price productPrice = null;
		
		ProductPriceEntity prodEntity = getProductById(productId);
		
		if(prodEntity!=null) {
			productPrice = new Price() ;
			productPrice.setValue(prodEntity.getPrice());
			productPrice.setCurrency_code(prodEntity.getCurrencyCode());
		}
		
		return productPrice;
	}

	public void modifyProdct(Product product) {
		// TODO Auto-generated method stub
		System.out.println("Price to be updated as "+ product.getCurrent_price().getValue());
		ProductPriceEntity  updatedProd = getProductById(product.getId());
		updatedProd.setPrice(product.getCurrent_price().getValue());
		// updatedProd = new ProductPriceEntity(product.getId(), product.getCurrent_price().getValue(), product.getCurrent_price().getCurrency_code());
		save(updatedProd);
	}
	
	
	public void delete() {
		prodSvcHelper.delete();
	}
	
	public void save(ProductPriceEntity entity) {
		prodSvcHelper.saveOrUpdate(entity);
	}
}
