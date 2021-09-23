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

There are some data loaded for testing on local.

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
