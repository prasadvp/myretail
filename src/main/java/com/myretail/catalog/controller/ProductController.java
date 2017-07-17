package com.myretail.catalog.controller;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.catalog.exception.ExceptionConstants;
import com.myretail.catalog.helper.CommonUtil;
import com.myretail.catalog.model.Price;
import com.myretail.catalog.model.Product;
import com.myretail.catalog.model.SearchProductResponse;
import com.myretail.catalog.model.Status;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;
import com.myretail.catalog.service.ProductService;


@RestController
@RequestMapping(value = "/retail")
public class ProductController  {
	
	@Autowired
	public ProductService productSvc;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@RequestMapping(value ="/v1/products/{productId}", method=RequestMethod.GET)
	public SearchProductResponse getProducts(@PathVariable Long productId) {
		SearchProductResponse response = new SearchProductResponse();
		LOGGER.debug("ProductController:: SearchProductResponse :: Product ID from the request "+ productId);
		try {
			if(!CommonUtil.isValidProductId(productId)) {
				LOGGER.error("ProductController:: SearchProductResponse :: Invalid Product ID ");
				response = (SearchProductResponse) CommonUtil.buildResponse(response, Status.ERROR, ExceptionConstants.VALIDATION_EXCEPTION.getDescription(), 
						ExceptionConstants.VALIDATION_EXCEPTION.getErrorCode());
				
			}
			else {
				Product product = new Product();	
				//Parallel execution Start
				long execstartTime = System.currentTimeMillis();
				execstartTime = System.currentTimeMillis();
				CompletableFuture<Product> productAsync = productSvc.getProductNameAsyncMode(productId);
				CompletableFuture<Price>  priceAsync = productSvc.getProductPriceAsyncMode(productId);
				 // Wait until they are all done
		        CompletableFuture.allOf(productAsync,priceAsync).join();
		        long elapsedTime = System.currentTimeMillis() -execstartTime;
				LOGGER.debug("Total exec time for Parallel execution  {}", elapsedTime);
				product  = productAsync.get();
				Price current_price = priceAsync.get();
				product.setId(productId);
				product.setCurrent_price(current_price);
				if(product!=null && current_price !=null)
					LOGGER.debug("Prodcut Id :{} :: Product Name {} :: Product Price :{} ",productId, product.getName() , product.getCurrent_price().getValue());
				//Parallel execution End
				
				response.setProduct(product);
				response.setStatus("SUCCESS");
			}
			
		}catch (Exception ex) {
			LOGGER.error("Exception while retrieving the products "+ ex.getMessage());
		}
		return response;
	}
	

	
	@RequestMapping(value ="/v1/products", method=RequestMethod.POST)
	public void createProducts() {
		productSvc.delete();
		productSvc.save(new ProductPriceEntity(20,new BigDecimal(1000.00),"USD"));
		productSvc.save(new ProductPriceEntity(30,new BigDecimal(2000.00),"USD"));
		productSvc.save(new ProductPriceEntity(13860428,new BigDecimal(100.00),"USD"));
	}
	
	@RequestMapping(value ="/v1/products/{productId}", method=RequestMethod.PUT)
	public void  modifyProduct(@RequestBody Product product) {
	
			productSvc.modifyProdct(product);
			
			//Testing
			
			Price updatedPrice  = productSvc.getProductPrice(product.getId());
			if(updatedPrice!=null) {
				LOGGER.debug("Update Value  {}" ,  updatedPrice.getValue());
			}
		
		
		
	}

}
