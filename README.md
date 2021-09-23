# Online Store API


### Background

This springboot maven project is a POC for EVM backend test.\
To avoid the race condition issue during 12.12 event, I am using `optimistic-lock-handing`.\
Using optimistic-lock-handling, the order process could be retried. Before processing the order, the stock quantity is
checked to avoid negative quantity. 

### Prerequisites

* Java 8

### Technologies

* Spring Boot (version 2.5.4)
* JPA/Hibernate
* Rest with Spring
* Error handing in REST
* H2 in-memory database

### Running the unit test

From terminal, execute `./mvnw verify`

### Running the api on local

From terminal, execute `./mvnw spring-boot:run`


### Endpoints:

* `POST /api//orders/process` to process the orders, it accepts request body:
```json
{
	"customerId": 1,
	"addressId": 2,
	"items": [
		{
			"productId": 3,
			"quantity": 5
		},
				{
			"productId": 2,
			"quantity": 15
		}
	]
}
```

* `GET /api/orders` to return all orders
* `GET /api/orders/{id}` to return orders by id
* `GET /api/product` to return all products
* `GET /api/product/{id}` to return product by id
* `GET /api/customer` to return all customers
* `GET /api/customer/{id}` to return customer by id
* `POST /api/product/addStock` to add stock into a product, it accepts request body:
```json
{
	"productId": 1,
	"quantity": 10
}
```
The endpoints are secured with basic auth, `username: user` and `password: password`

Above endpoints available for test on http://antasena-online-store.herokuapp.com/

There are some data loaded for testing
|CUSTOMER_ID|FIRST_NAME|LAST_NAME|EMAIL|
|----------------|----------------|----------------|----------------|
|1|John|Doe|john.doe@mail.com|
|2|Jane|Doe|jane.doe@mail.com|
|3|Josh|Doe|josh.doe@mail.com|

|ADDRESS_ID|ADDRESS_LINE1|ADDRESS_LINE2|POSTCODE|
|----------------|----------------|----------------|----------------|
|1|Jl Kaliurang no 100|Yogyakarta|9091|
|2|Jl Gejayan no 002|Yogyakarta|9092|
|3|Jl Malioboro no 3|Yogyakarta|9093|

|PRODUCT_ID|NAME|PRICE|UNIT_IN_STOCK|
|----------------|----------------|----------------|----------------|
|1|Product 1|50000.00|100|
|2|Product 2|25000.00|50|
|3|Product 3|100000.00|10|
|4|Product 4|10000.00|75|
|5|Product 5|20000.00|20|

