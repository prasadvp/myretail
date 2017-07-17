package com.myretail.catalog.helper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.catalog.model.Price;
import com.myretail.catalog.model.Product;


public class ProductDetailsDeserializer extends JsonDeserializer<Product> {

	@Override
	public Product deserialize(JsonParser jp, DeserializationContext context)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		JsonNode prodRootNode = node.get("product");
		Product prodObj = new Product();
		if (prodRootNode.get("current_price") != null) {
			prodObj.setId(prodRootNode.get("product_id").asLong());
			Price price = new ObjectMapper().treeToValue(prodRootNode.get("current_price"), Price.class);
			prodObj.setCurrent_price(price);
		} else {
			String productTitle = node.get("product").get("item").get("product_description").get("title").asText();

			prodObj.setName(productTitle);
		}
		return prodObj;

	}

}
