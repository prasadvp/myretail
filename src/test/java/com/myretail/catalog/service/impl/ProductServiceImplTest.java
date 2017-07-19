package com.myretail.catalog.service.impl;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.myretail.catalog.exception.RetailServiceException;
import com.myretail.catalog.helper.RetailConstants;
import com.myretail.catalog.model.Price;
import com.myretail.catalog.model.Product;
import com.myretail.catalog.persistence.dao.ProductDAO;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;
import com.myretail.catalog.persistence.repoistory.ProductPriceRepoistory;
import com.myretail.catalog.service.ProductService;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.properties")
public class ProductServiceImplTest {

	@MockBean
	@Qualifier("productDAO")
	private ProductDAO prodDAO;
	
	
	 @Autowired
	private ProductService prodService;
	
	@MockBean
	@Qualifier("ProductPriceRepoistory")
	private ProductPriceRepoistory prodPriceRepo;
	

	
	@TestConfiguration
    static class ProductServiceImplTestContextConfiguration {
  
        @Bean
        public ProductService productService() {
            return new ProductServiceImpl();
        }
    }
	
	
	@Before
	public void setUp() throws Exception {
	
		
	}

	

	@Test
	public final void testModifyProduct() {
		ProductPriceEntity product = new ProductPriceEntity("13860428", BigDecimal.valueOf(1000.00), "USD");
		//Mockito.when(prodDAO.getProductById(Mockito.anyString())).thenReturn(product);
		Product prod = new Product();
		prod.setId(13860428);
		Price price = new Price();
		price.setCurrency_code("USD");
		price.setValue(BigDecimal.valueOf(1000.00));
		prod.setCurrent_price(price);
		Mockito.when(prodDAO.update(Mockito.anyObject())).thenReturn(product);
		
		assertEquals(Long.toString(prod.getId()), product.getProductId());
		assertEquals(prod.getCurrent_price().getCurrency_code(), product.getCurrencyCode());
		assertEquals(prod.getCurrent_price().getValue(), product.getPrice());
		
		
	}

	

	@Test 
	public final void testModifyInvalidProduct() {
		ProductPriceEntity product = new ProductPriceEntity("13860428", BigDecimal.valueOf(1000.00), "USD");
		//Mockito.when(prodDAO.getProductById(Mockito.anyString())).thenReturn(product);
		Product prod = new Product();
		prod.setId(13860428);
		Price price = new Price();
		price.setCurrency_code("USD");
		price.setValue(BigDecimal.valueOf(1000.00));
		prod.setCurrent_price(price);
		Mockito.when(prodDAO.update(Mockito.anyObject())).thenThrow(new RetailServiceException(RetailConstants.PRODUCT_OBJ_NOT_FOUND_EXCEPTION.getCode(), RetailConstants.PRODUCT_OBJ_NOT_FOUND_EXCEPTION.getDescription()));
		
	}

	@Test
	public final void testGetProductDetail() {
		
		String productName = "The Big Lebowski (Blu-ray)";
		BigDecimal price = BigDecimal.valueOf(1000.00);
		ProductPriceEntity product = new ProductPriceEntity("13860428", BigDecimal.valueOf(1000.00), "USD");
		
		Mockito.when(prodDAO.getProductById(Mockito.anyString())).thenReturn(product);
		Product prod = prodService.getProductDetail(13860428);
		
		assertThat(prod.getName(),is(equalTo(productName)));
		
		assertThat(price,  Matchers.comparesEqualTo(prod.getCurrent_price().getValue()));
		
	}
	
	@Test
	public final void testGetProductDetailForTitleMismatch() {
		
		String productName = "They Big Lebowski (Blu-ray)";
		
		ProductPriceEntity product = new ProductPriceEntity("13860428", BigDecimal.valueOf(1000.00), "USD");
		
		Mockito.when(prodDAO.getProductById(Mockito.anyString())).thenReturn(product);
		Product prod = prodService.getProductDetail(13860428);
		
		assertNotEquals(productName, prod.getName());
		
	}
	
	
	@Test
	public final void testGetProductDetailForPriceNotFound() {
		ProductPriceEntity product = new ProductPriceEntity("100", BigDecimal.valueOf(1000.00), "USD");
		
		Mockito.when(prodDAO.getProductById(Mockito.anyString())).thenReturn(product);
		Product prod = prodService.getProductDetail(13860428);
		assertNotEquals(prod.getId(), product.getProductId());
	}
	
	
	
	

}
