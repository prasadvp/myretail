package com.myretail.catalog.helper;



import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.catalog.exception.RetailServiceException;
import com.myretail.catalog.model.Price;
import com.myretail.catalog.model.Product;

public class ProductDetailsDeserializer extends JsonDeserializer<Product> {

	public static final String PRODUCT_NODE = "product";
	public static final String ITEM_NODE = "item";
	public static final String CURRENT_PRICE_NODE = "current_price";
	public static final String PRODUCT_ID_NODE = "product_id";
	public static final String PRODUCT_DESCRIPTION_NODE = "product_description";
	public static final String TITLE_NODE = "title";
	
	@Override
	public Product deserialize(JsonParser jp, DeserializationContext context)  {

		Product prodObj = new Product();
		try {
			ObjectCodec oc = jp.getCodec();
			JsonNode node = oc.readTree(jp);
			JsonNode prodRootNode = node.get(PRODUCT_NODE);
			
			if (prodRootNode.get(ITEM_NODE) != null) {
				JsonNode itemNode = prodRootNode.get(ITEM_NODE);
				String productTitle = getProductTitle(itemNode);
				prodObj.setName(productTitle);
			} else if (prodRootNode.get(PRODUCT_ID_NODE)!=null && prodRootNode.get(CURRENT_PRICE_NODE) != null) {
				prodObj.setId(prodRootNode.get(PRODUCT_ID_NODE).asLong());
				Price price = new ObjectMapper().treeToValue(prodRootNode.get(CURRENT_PRICE_NODE), Price.class);
				price.getValue().setScale(2, BigDecimal.ROUND_HALF_UP);
				prodObj.setCurrent_price(price);
			}
			
		}catch (Exception ex) {
			throw new RetailServiceException(RetailConstants.JSON_DESERIALIZE_EXCEPTION.getCode(),
					RetailConstants.JSON_DESERIALIZE_EXCEPTION.getDescription(),ex.getMessage());
		}
		
		return prodObj;

	}

	private String getProductTitle(JsonNode itemNode) {
		String productTitle = "";
		if (itemNode.get(PRODUCT_DESCRIPTION_NODE) != null && itemNode.get(PRODUCT_DESCRIPTION_NODE).get(TITLE_NODE) != null) {
			productTitle = itemNode.get(PRODUCT_DESCRIPTION_NODE).get(TITLE_NODE).asText();
		}
		return productTitle;
	}

}
