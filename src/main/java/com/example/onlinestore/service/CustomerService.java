package com.example.onlinestore.service;

import com.example.onlinestore.model.Customer;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Iterable<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Transactional
    public Long saveCustomer(Customer customer) {
        if (customer.getId() == null) {
            customer.setCreatedDate(new Date());
        }
        customer.setLastUpdatedDate(new Date());
        Customer newCustomer = customerRepository.save(customer);
        return newCustomer.getId();
    }

    @Transactional
    public void deleteCustomertById(Long productId) {
        Customer customer = customerRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
        deleteProduct(customer);
    }

    @Transactional
    public void deleteProduct(Customer customer) {
        customer.setRecordStatus(0);
        saveCustomer(customer);
    }
}
