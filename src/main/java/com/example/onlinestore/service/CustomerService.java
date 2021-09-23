package com.example.onlinestore.service;

import com.example.onlinestore.exceptionhandling.RecordNotFoundException;
import com.example.onlinestore.model.Customer;
import com.example.onlinestore.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException(Customer.class, customerId));
    }

    @Transactional(readOnly = true)
    public Iterable<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Transactional
    public Long saveCustomer(Customer customer) {
        log.debug("Saving customer");
        if (customer.getId() == null) {
            customer.setCreatedDate(new Date());
        }
        customer.setLastUpdatedDate(new Date());
        Customer newCustomer = customerRepository.save(customer);
        return newCustomer.getId();
    }

    @Transactional
    public void deleteCustomertById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException(Customer.class, customerId));
        deleteProduct(customer);
    }

    @Transactional
    public void deleteProduct(Customer customer) {
        log.debug("Deleting customer: ", customer.getId());
        customer.setRecordStatus(0);
        customer.setLastUpdatedDate(new Date());
        saveCustomer(customer);
    }
}
