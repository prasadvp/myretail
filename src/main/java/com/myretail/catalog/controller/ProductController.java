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
/**
 * This method used to get the product details (name and price) from two different sources 
 * for given product id. 
 * It fetches the information in async mode , consolidate the response and return it.
 * it validates whether the input product id is valid - > 0, product name  or price is null
 * and throws exception with proper httpstatus code is being set
 * All exceptions are wrapped as RetailServiceException
 * @param productId
 * @param httpResponse
 * @return
 */
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
/**
 * This method evaluate the product name and price for null conditions and set 
 * appropriate message in the response object
 * @param product
 * @param response
 */
	

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
	/**
	 * This is a test method used for creating the products at the start of execution
	 */

	@RequestMapping(value = "/v1/products", method = RequestMethod.POST)
	public void createProducts() {
		productSvc.delete();
		productSvc.save(new ProductPriceEntity("13860427", BigDecimal.valueOf(1000.00), "USD"));
		productSvc.save(new ProductPriceEntity("13860428", BigDecimal.valueOf(2000.00), "USD"));
		productSvc.save(new ProductPriceEntity("13860429", BigDecimal.valueOf(100.00), "USD"));
	

	}
/**
 * This method will modify the price information of a product. It also checks for the valid
 * product id, price information is present and return success /error message. 
 * In case of error, the RetailServiceException is being thrown from the internal methods which 
 * used to set the error code and message in the response object
 * @param product
 * @param httpResponse
 * @return
 */
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
/**
 * This method to check whether the product sent in the request is valid. this method
 * is called from {@link #modifyProduct(	Request Body, HttpServletResponse)} modifyProduct method. it checks for valid product id 
 * and product price to be updated is > 0
 * @param product
 * @param response
 */
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

	/**
	 * This is a util method called to check whether product id is > 0 
	 * @param id
	 * @return
	 */
	private boolean isValidProductId(long id) {
		return (id > 0) ? true : false;
	}
/**
 * This method to build the Message object for the given code and description
 * @param code
 * @param description
 * @return
 */
	private Message buildMessage(String code, String description) {
		return new Message(code, description);
	}

	private void logDebugMsg(String msg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(msg);
		}

	}
	
	

}
