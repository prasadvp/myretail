package com.myretail.catalog.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.catalog.exception.RetailServiceException;
import com.myretail.catalog.helper.RetailConstants;
import com.myretail.catalog.model.Message;
import com.myretail.catalog.model.Product;
import com.myretail.catalog.model.ProductResponse;
import com.myretail.catalog.model.Status;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;
import com.myretail.catalog.service.ProductService;

@RestController
@RequestMapping(value = "/retail")
public class ProductController {

	@Autowired
	@Qualifier("productService")
	public ProductService productSvc;


	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@RequestMapping(value = "/v1/products/{productId}", method = RequestMethod.GET)
	public ProductResponse getProducts(@PathVariable Long productId, HttpServletResponse httpResponse) {
		ProductResponse response = new ProductResponse();

		logDebugMsg("ProductController:: SearchProductResponse :: Product ID from the request " + productId);

		try {
			if (isValidProductId(productId)) {
				
				Product product = productSvc.getProductDetail(productId);
				
				checkAndSetProdResponse(product, response);
				if (Status.ERROR.value().equalsIgnoreCase(response.getStatus())) {
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else {
					

					response.setProduct(product);
					response.setStatus(Status.SUCCESS.value());
				}

			} else {
				LOGGER.error("ProductController:: SearchProductResponse :: Invalid Product ID ");
				response.setStatus(Status.ERROR.value());
				response.getMessages().add(buildMessage(RetailConstants.PRODUCT_ID_VALIDATION_EXCEPTION.getCode(),
						RetailConstants.PRODUCT_ID_VALIDATION_EXCEPTION.getDescription()));
				httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (RetailServiceException ex) {
			LOGGER.error("Exception while retrieving the products " + ex.getMessage());
			response.getMessages().add(buildMessage(ex.getErrorCode(), ex.getDescription()));
			response.setStatus(Status.ERROR.value());
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	

	private void checkAndSetProdResponse(Product product, ProductResponse response) {
		if (product == null || product.getName()==null) {
			response.setStatus(Status.ERROR.value());
			response.getMessages().add(buildMessage(RetailConstants.PRODUCT_NAME_NOT_FOUND_EXCEPTION.getCode(),
					RetailConstants.PRODUCT_NAME_NOT_FOUND_EXCEPTION.getDescription()));

		}
		if (product == null || product.getCurrent_price() == null) {
			response.setStatus(Status.ERROR.value());
			response.getMessages().add(buildMessage(RetailConstants.PRODUCT_PRICE_NOT_FOUND_EXCEPTION.getCode(),
					RetailConstants.PRODUCT_PRICE_NOT_FOUND_EXCEPTION.getDescription()));

		}

	}

	@RequestMapping(value = "/v1/products", method = RequestMethod.POST)
	public void createProducts() {
		productSvc.delete();
		productSvc.save(new ProductPriceEntity("13860427", BigDecimal.valueOf(1000.00), "USD"));
		productSvc.save(new ProductPriceEntity("13860428", BigDecimal.valueOf(2000.00), "USD"));
		productSvc.save(new ProductPriceEntity("13860429", BigDecimal.valueOf(100.00), "USD"));
	

	}

	@RequestMapping(value = "/v1/products/{productId}", method = RequestMethod.PUT)
	public ProductResponse modifyProduct(@RequestBody Product product, HttpServletResponse httpResponse) {

		ProductResponse response = new ProductResponse();
		try {
			if (product != null) {

				isValidRequest(product, response);
				if (Status.ERROR.value().equalsIgnoreCase(response.getStatus())) {
					httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}

				else {
					productSvc.modifyProduct(product);
					response.setStatus(Status.SUCCESS.value());
					response.getMessages().add(buildMessage(RetailConstants.PRODUCT_UPDATE_SUCCESSFUL.getCode(),
							RetailConstants.PRODUCT_UPDATE_SUCCESSFUL.getDescription()));
				}

			} else {
				response.setStatus(Status.ERROR.value());
				response.getMessages().add(buildMessage(RetailConstants.PRODUCT_OBJ_NOT_FOUND_EXCEPTION.getCode(),
						RetailConstants.PRODUCT_OBJ_NOT_FOUND_EXCEPTION.getDescription()));
				
				httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (RetailServiceException ex) {
			LOGGER.error("Exception while Updating the products " + ex.getMessage());
			response.getMessages().add(buildMessage(ex.getErrorCode(), ex.getDescription()));
			response.setStatus(Status.ERROR.value());
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		return response;

	}

	private void isValidRequest(Product product, ProductResponse response) {
		if (!isValidProductId(product.getId())) {
			response.getMessages().add(buildMessage(RetailConstants.PRODUCT_ID_VALIDATION_EXCEPTION.getCode(),
					RetailConstants.PRODUCT_ID_VALIDATION_EXCEPTION.getDescription()));
		}
		if (product.getCurrent_price() == null
				|| product.getCurrent_price().getValue().compareTo(BigDecimal.ZERO) <= 0) {
			response.getMessages().add(buildMessage(RetailConstants.PRODUCT_PRICE_VALIDATION_EXCEPTION.getCode(),
					RetailConstants.PRODUCT_PRICE_VALIDATION_EXCEPTION.getDescription()));
		}

		if (!response.getMessages().isEmpty())
			response.setStatus(Status.ERROR.value());

	}

	private boolean isValidProductId(long id) {
		return (id > 0) ? true : false;
	}

	private Message buildMessage(String code, String description) {
		return new Message(code, description);
	}

	private void logDebugMsg(String msg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(msg);
		}

	}
	
	

}
