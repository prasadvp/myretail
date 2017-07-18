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

export JAVA_HOME=$(/usr/libexec/java_home)
export M2_HOME=/Users/VP/Documents/Software/apache-maven-3.5.0
export PATH=$PATH:$M2_HOME/bin
export MANGO_HOME=/Users/VP/Documents/Software/mongodb-osx-x86_64-3.4.6
export PATH=$PATH:$MANGO_HOME/bin

Import it as standard maven project in IDE of your choice. As a last step, run "mvn clean package" and find the jar in the target folder 


## Running ##

From  the terminal, start the mongoDB instance. For Example, 

mongod --dbpath <path to data directory>

Run the "Application.java" as standard java application. Spring boot will start and load the context information. Application is ready to use. 

Application will be accessible in localhost:8080

## How to use it ##

###Create  product###

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
###Fetch Products ###

This service will take the product id as path parameter, does basic validation, retrieves the product name from an external API and price information from local NOSQL Mongog DB. Both of them are executed asynchronously for quick turnaround. The Product information are cached for future requests which helps in quick reterival

```java
@RequestMapping(value = "/v1/products/{productId}", method = RequestMethod.GET)
	public ProductResponse getProducts(@PathVariable Long productId, HttpServletResponse httpResponse)
	
	HTTP Method: GET
	Headers: 
	Content-Type: application/json
	http://localhost:8080/retail/v1/products/13860428
	
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