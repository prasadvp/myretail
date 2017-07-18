package com.myretail.catalog.controller;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.myretail.catalog.model.Price;
import com.myretail.catalog.model.Product;
import com.myretail.catalog.persistence.dao.ProductDAO;
import com.myretail.catalog.persistence.repoistory.ProductPriceRepoistory;
import com.myretail.catalog.service.ProductService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class, secure = false)

public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	@Qualifier("productService")
	private ProductService prodService;

	@MockBean
	@Qualifier("productDAO")
	private ProductDAO prodDAO;

	@MockBean
	@Qualifier("ProductPriceRepoistory")
	private ProductPriceRepoistory prodPriceRepo;

	RequestBuilder controllerRequestGetBuilder;

	RequestBuilder controllerRequestPutBuilder;
	
	

	@Before
	public void setUp() throws Exception {

		controllerRequestGetBuilder = MockMvcRequestBuilders.get("/retail/v1/products/10")
				.accept(MediaType.APPLICATION_JSON);

		controllerRequestPutBuilder = MockMvcRequestBuilders.put("/retail/v1/products/10")
				.accept(MediaType.APPLICATION_JSON);
	}

	@Test
	public final void testGetProducts() throws Exception {

		Product product = new Product();
		product.setId(10);
		product.setName("TestProduct");
		Price price = new Price();
		price.setCurrency_code("USD");
		price.setValue(BigDecimal.valueOf(100.00));
		product.setCurrent_price(price);
		Mockito.when(prodService.getProductDetail(Mockito.anyLong())).thenReturn(product);
		MvcResult result = mockMvc.perform(controllerRequestGetBuilder).andExpect(status().isOk()).andReturn();
		String expected = "{\"status\":\"SUCCESS\",\"messages\":[],\"product\":{\"current_price\":{\"value\":100.0,\"currency_code\":\"USD\"},\"product_id\":10,\"title\":\"TestProduct\"}}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	@Test
	public final void testGetInvalidProductId() throws Exception {

		controllerRequestGetBuilder = MockMvcRequestBuilders.get("/retail/v1/products/0")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(controllerRequestGetBuilder).andExpect(status().is4xxClientError()).andReturn();
		String expected = "{\"status\":\"ERROR\",\"messages\":[{\"code\":\"RSVC-4000\",\"description\":\"Invalid Product ID\"}],\"product\":null}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}
	
	@Test
	public final void testGetProductIdwithNoDetails() throws Exception {

		Product product = new Product();
		product.setId(100);
		product.setName(null);
		product.setCurrent_price(null);
		controllerRequestGetBuilder = MockMvcRequestBuilders.get("/retail/v1/products/100")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(controllerRequestGetBuilder).andExpect(status().is5xxServerError()).andReturn();
		String expected = "{\"status\":\"ERROR\",\"messages\":[{\"code\":\"RSVC-4001\",\"description\":\"Unable to find Product Name\"},{\"code\":\"RSVC-4002\",\"description\":\"Unable to find Product Price\"}],\"product\":null}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	@Test
	public final void testGetProductIdOnlyName() throws Exception {

		Product product = new Product();
		product.setId(100);
		product.setName("Test");
		product.setCurrent_price(null);
		controllerRequestGetBuilder = MockMvcRequestBuilders.get("/retail/v1/products/100")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(controllerRequestGetBuilder).andExpect(status().is5xxServerError()).andReturn();
		String expected = "{\"status\":\"ERROR\",\"messages\":[{\"code\":\"RSVC-4001\",\"description\":\"Unable to find Product Name\"},{\"code\":\"RSVC-4002\",\"description\":\"Unable to find Product Price\"}],\"product\":null}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}
	
	@Test
	public final void testGetProductIdOnlyPrice() throws Exception {

		Product product = new Product();
		product.setId(100);
		product.setName(null);
		Price price = new Price();
		price.setCurrency_code("USD");
		price.setValue(BigDecimal.valueOf(100.00));
		product.setCurrent_price(price);
		controllerRequestGetBuilder = MockMvcRequestBuilders.get("/retail/v1/products/100")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(controllerRequestGetBuilder).andExpect(status().is5xxServerError()).andReturn();
		String expected = "{\"status\":\"ERROR\",\"messages\":[{\"code\":\"RSVC-4001\",\"description\":\"Unable to find Product Name\"},{\"code\":\"RSVC-4002\",\"description\":\"Unable to find Product Price\"}],\"product\":null}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}
	@Test
	public final void testModifyProduct() throws Exception {
		String modifyProdJson = "{\n" + 
				"\"product\": {\n" + 
				"\"current_price\": {\n" + 
				"\"value\": 100,\n" + 
				"\"currency_code\": \"USD\"\n" + 
				"},\n" + 
				"\"product_id\": 10,\n" + 
				"\"title\": \"Test Product\"\n" + 
				"}\n" + 
				"}";
		Product product = new Product();
		product.setId(10);
		product.setName("TestProduct");
		Price price = new Price();
		price.setCurrency_code("USD");
		price.setValue(BigDecimal.valueOf(100.00));
		product.setCurrent_price(price);
		Mockito.when(prodService.getProductDetail(Mockito.anyLong())).thenReturn(product);
		controllerRequestPutBuilder = MockMvcRequestBuilders
				.put("/retail/v1/products/10")
				.accept(MediaType.APPLICATION_JSON).content(modifyProdJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(controllerRequestPutBuilder).andExpect(status().isOk()).andReturn();
		String expected = "{\"status\":\"SUCCESS\",\"messages\":[{\"code\":\"RSVC-2000\",\"description\":\"Product details updated successfully\"}],\"product\":null}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public final void testModifyProductInvalidProductId() throws Exception {
		String modifyProdJson = "{\n" + 
				"\"product\": {\n" + 
				"\"current_price\": {\n" + 
				"\"value\": 100,\n" + 
				"\"currency_code\": \"USD\"\n" + 
				"},\n" + 
				"\"product_id\": 0,\n" + 
				"\"title\": \"Test Product\"\n" + 
				"}\n" + 
				"}";
		Product product = new Product();
		product.setId(10);
		product.setName("TestProduct");
		Price price = new Price();
		price.setCurrency_code("USD");
		price.setValue(BigDecimal.valueOf(100.00));
		product.setCurrent_price(price);
		Mockito.when(prodService.getProductDetail(Mockito.anyLong())).thenReturn(product);
		controllerRequestPutBuilder = MockMvcRequestBuilders
				.put("/retail/v1/products/0")
				.accept(MediaType.APPLICATION_JSON).content(modifyProdJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(controllerRequestPutBuilder).andExpect(status().is4xxClientError()).andReturn();
		String expected = "{\"status\":\"ERROR\",\"messages\":[{\"code\":\"RSVC-4000\",\"description\":\"Invalid Product ID\"}],\"product\":null}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public final void testModifyProductInvalidProductPrice() throws Exception {
		String modifyProdJson = "{\n" + 
				"\"product\": {\n" + 
				"\"current_price\": {\n" + 
				"\"value\": 0,\n" + 
				"\"currency_code\": \"USD\"\n" + 
				"},\n" + 
				"\"product_id\": 100,\n" + 
				"\"title\": \"Test Product\"\n" + 
				"}\n" + 
				"}";
		Product product = null;
		Mockito.when(prodService.getProductDetail(Mockito.anyLong())).thenReturn(product);
		controllerRequestPutBuilder = MockMvcRequestBuilders
				.put("/retail/v1/products/0")
				.accept(MediaType.APPLICATION_JSON).content(modifyProdJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(controllerRequestPutBuilder).andExpect(status().is4xxClientError()).andReturn();
		String expected = "{\"status\":\"ERROR\",\"messages\":[{\"code\":\"RSVC-4003\",\"description\":\"Invalid Product Price \"}],\"product\":null}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	

	@Test
	public final void testModifyProductNullProductId() throws Exception {
		String modifyProdJson = "{\n" + 
				"\"product\": {\n" + 
				"\"current_price\": {\n" + 
				"\"value\": 100,\n" + 
				"\"currency_code\": \"USD\"\n" + 
				"},\n" + 
				"\"product_id\": null,\n" + 
				"\"title\": \"Test Product\"\n" + 
				"}\n" + 
				"}";
		Product product = new Product();
		product.setId(10);
		product.setName("TestProduct");
		Price price = new Price();
		price.setCurrency_code("USD");
		price.setValue(BigDecimal.valueOf(100.00));
		product.setCurrent_price(price);
		Mockito.when(prodService.getProductDetail(Mockito.anyLong())).thenReturn(product);
		controllerRequestPutBuilder = MockMvcRequestBuilders
				.put("/retail/v1/products/0")
				.accept(MediaType.APPLICATION_JSON).content(modifyProdJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(controllerRequestPutBuilder).andExpect(status().is4xxClientError()).andReturn();
		String expected = "{\"status\":\"ERROR\",\"messages\":[{\"code\":\"RSVC-4000\",\"description\":\"Invalid Product ID\"}],\"product\":null}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	

}
