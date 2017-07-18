package com.myretail.catalog.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.myretail.catalog.service.ProductService;
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class, secure = false)
public class ProductControllerTest {
	
	

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testGetProducts() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testModifyProduct() {
		fail("Not yet implemented"); // TODO
	}
	/*
	 * testForInvalidProductId
	 * testForProductId that does not have name and price
	 * testForProductID that has both name and Price
	 * testForProductID that has name but not price
	 * testForProductID that has price but not name
	 * ModifyProduct - Valid details
	 * ModifyProduct - Invalid product ID
	 * ModifyProduct - Product ID that does not exist
	 * ModifyProduct - Product ID that has null price
	 * ModifyProduct - Product ID that has price <= 0.0
	 * Modify Product - Product ID that has valid price and product 
	 */
}
