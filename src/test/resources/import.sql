--customer data
insert into CUSTOMER(first_name, last_name, phone_number, email, created_date, record_status, version) values ('John', 'Doe', '0123456789', 'john.doe@mail.com', CURRENT_TIMESTAMP(), 1, 1);
insert into CUSTOMER(first_name, last_name, phone_number, email, created_date, record_status, version) values ('Jane', 'Doe', '0012345678', 'jane.doe@mail.com', CURRENT_TIMESTAMP(), 1, 1);
insert into CUSTOMER(first_name, last_name, phone_number, email, created_date, record_status, version) values ('Josh', 'Doe', '0002345678', 'josh.doe@mail.com', CURRENT_TIMESTAMP(), 1, 1);

--product data
insert into PRODUCT(name, description, price, unit_in_stock, created_date, record_status, version) values ('Product 1', 'Description for product 1', 50000, 100, CURRENT_TIMESTAMP(), 1, 1);
insert into PRODUCT(name, description, price, unit_in_stock, created_date, record_status, version) values ('Product 2', 'Description for product 2', 25000, 50, CURRENT_TIMESTAMP(), 1, 1);
insert into PRODUCT(name, description, price, unit_in_stock, created_date, record_status, version) values ('Product 3', 'Description for product 3', 100000, 10, CURRENT_TIMESTAMP(), 1, 1);
insert into PRODUCT(name, description, price, unit_in_stock, created_date, record_status, version) values ('Product 4', 'Description for product 4', 10000, 75, CURRENT_TIMESTAMP(), 1, 1);
insert into PRODUCT(name, description, price, unit_in_stock, created_date, record_status, version) values ('Product 5', 'Description for product 5', 20000, 20, CURRENT_TIMESTAMP(), 1, 1);

--address data
insert into ADDRESS(address_line1, address_line2, postcode, customer_id, created_date, record_status, version) values ('Jl Kaliurang no 100', 'Yogyakarta', '9091', 1, CURRENT_TIMESTAMP(), 1, 1);
insert into ADDRESS(address_line1, address_line2, postcode, customer_id, created_date, record_status, version) values ('Jl Gejayan no 002', 'Yogyakarta', '9092', 2, CURRENT_TIMESTAMP(), 1, 1);
insert into ADDRESS(address_line1, address_line2, postcode, customer_id, created_date, record_status, version) values ('Jl Malioboro no 3', 'Yogyakarta', '9093', 3, CURRENT_TIMESTAMP(), 1, 1);