package com.myretail.catalog.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.myretail.catalog.persistence.dao.ProductDAO;
import com.myretail.catalog.persistence.entity.ProductPriceEntity;
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.properties")
@SpringBootTest
@DirtiesContext(classMode=ClassMode.AFTER_CLASS) 
public class ProductDAOImplTest {
	

	
	@Autowired
	private ProductDAO prodDAO;
	
	
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Rollback(true)
	public final void testCreate() {
		  
		
		prodDAO.create(new ProductPriceEntity("100", BigDecimal.valueOf(1000.00), "USD"));
		
		 ProductPriceEntity entity = prodDAO.getProductById("100");
		assertEquals("100", entity.getProductId());
		
	}
	
	@Test
	public final void testgetProductById() {
		ProductPriceEntity entity = prodDAO.getProductById("13860428");
		assertEquals("13860428", entity.getProductId());
	}
	
	@Test
	public final void testProductIDNotExist() {
		ProductPriceEntity entity = prodDAO.getProductById("200");
		assertNull(entity);
	}
	
	
	
	@Test
	@Rollback(true)
	public final void testUpdatePrice() {
		prodDAO.update(new ProductPriceEntity("100", BigDecimal.valueOf(3000.00), "USD"));
		 ProductPriceEntity entity = prodDAO.getProductById("100");
		 assertEquals(BigDecimal.valueOf(3000.00), entity.getPrice());
		
	}
}
