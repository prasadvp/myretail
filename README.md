# MyRetail Rest Product Catalog #
1. Fetch the product details using Product ID
2. Product Title and Price Detail are fetched asynchronously, collated and returned
3. Product details are fetched from External APIand product title is retrieved from it
4. Modify the product price information by taking the product id, title and price details. 
5. ModifyProduct service was secured and only users with valid Auth Token can update the product

## Development  ## 
Java 8+
Spring Boot 3.5.4
Mongo DB 3.4.6
Sonarlint for code analysis 
Maven for build 3.3+
Spring tool suite as Editor 
SourceTree for code management 
Advanced Rest Client for testing

## Installation ## 

* `git clone` this repository

* set the JAVA_HOME , M2_HOME, MongoDB as environment variable. 

Replace the folder location as appropriate 
For Example, in MAC edit .bash_profile in Terminal and set the following : 

<pre>

export JAVA_HOME=$(/usr/libexec/java_home)
export M2_HOME=/Users/VP/Documents/Software/apache-maven-3.5.0
export PATH=$PATH:$M2_HOME/bin
export MANGO_HOME=/Users/VP/Documents/Software/mongodb-osx-x86_64-3.4.6
export PATH=$PATH:$MANGO_HOME/bin

</pre>

Import it as standard maven project in IDE of your choice. As a last step, run "mvn clean package" and find the jar in the target folder 


## Running ##

From  the terminal, start the mongoDB instance. For Example, 

mongod --dbpath <path to data directory>

Run the "Application.java" as standard java application. Spring boot will start and load the context information. Application is ready to use. 

Application will be accessible in localhost:8080

## How to use it ##

### Create  product ###

This is an one time activity to initially clear the existing product price details and load the pre-defined price information into DB. Please note that this service is required and it is mandatory to pass the AUTH-TOKEN information

In Advanced Rest Client, create a project and add the below URL:

Method: Post
Headers:
    Content-Type: application/json
    Auth-Token: password

http://localhost:8080/retail/v1/products

The product price information can be located in ProductController file 

```java

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
```
### Fetch Products ###

This service will take the product id as path parameter, does basic validation, retrieves the product name from an external API and price information from local NOSQL database. Both of them are executed asynchronously for quick turnaround. The Product information are cached for future requests which helps in quick reterival

```java

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
	public ProductResponse getProducts(@PathVariable Long productId, HttpServletResponse httpResponse)
	
	
Sample Request / Response 

	HTTP Method: GET
	Headers: 
	Content-Type: application/json

URI: 	http://localhost:8080/retail/v1/products/13860428
	
	Response: 
    	{
        "status": "SUCCESS",
        "messages": [],
        "product": {
        "current_price": {
        "value": 140,
        "currency_code": "USD"
        },
        "product_id": 13860428,
        "title": "The Big Lebowski (Blu-ray)"
        }
    }
	
	
```
### Modify Product ###

This service is used to update the price detail for given product. It validates the product id and price information, updates the price detail in database. It also update the cache with latest price information. This service checks for the auth token from the headers, validates and only if successful allow the user to update the price information 
```java
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
	public ProductResponse modifyProduct(@RequestBody Product product, HttpServletResponse httpResponse) 
	
	
	
Sample Request / Response:

HTTP Method: PUT
Headers:
Content-Type: application/json
Auth-Token: password

URL: http://localhost:8080/retail/v1/products/13860428


{
"product": {
"current_price": {
"value": 140,
"currency_code": "USD"
},
"product_id": 13860428,
"title": "The Big Lebowski (Blu-ray)"
}
}


Response:

{
"status": "SUCCESS",
"messages": [
  {
"code": "RSVC-2000",
"description": "Product details updated successfully"
}
],
"product": null
}

```

### Error Handling ### 
There are defined error codes and descriptions which will be set on different scenarios. This will help the user to take corrective actions  

Sample error codes:

``` java
	DEFAULT_EXCEPTION ("CERR-1000","Retail service Default Exception"),
	JSON_DESERIALIZE_EXCEPTION("CERR-1001", "Unable to deserialize JSON object"),
	WEB_SERVICE_EXCEPTION("WERR-5000", "Web service Exception "),
	PRODUCT_ID_VALIDATION_EXCEPTION("RSVC-4000", "Invalid Product ID"),
	PRODUCT_NAME_NOT_FOUND_EXCEPTION("RSVC-4001", "Unable to find Product Name"),
	PRODUCT_PRICE_NOT_FOUND_EXCEPTION("RSVC-4002", "Unable to find Product Price"),
	PRODUCT_PRICE_VALIDATION_EXCEPTION("RSVC-4003", "Invalid Product Price "),
	PRODUCT_OBJ_NOT_FOUND_EXCEPTION("RSVC-4004", "Product Information does not exist"),
	PRODUCT_UPDATE_SUCCESSFUL("RSVC-2000", "Product details updated successfully");


Sample error response:

Fetching product for Invalid ID:

GET: 
http://localhost:8080/retail/v1/products/0


Response:

    {
    "status": "ERROR",
    "messages": [
      {
    "code": "RSVC-4000",
    "description": "Invalid Product ID"
    }
    ],
    "product": null
    }


Product ID does not exist 

http://localhost:8080/retail/v1/products/100


    {
    "status": "ERROR",
    "messages": [
      {
    "code": "RSVC-4001",
    "description": "Unable to find Product Name"
    },
      {
    "code": "RSVC-4002",
    "description": "Unable to find Product Price"
    }
    ],
    "product": null
    }

```

# References #

[Spring boot] [https://projects.spring.io/spring-boot/]


