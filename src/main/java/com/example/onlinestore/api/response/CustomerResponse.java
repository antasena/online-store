package com.example.onlinestore.api.response;

import com.example.onlinestore.model.Customer;
import lombok.Data;

@Data
public class CustomerResponse {
    private Long customerId;
    private String fullName;
    private String email;
    private String phoneNumber;

    public CustomerResponse(Customer customer) {
        this.customerId = customer.getId();
        this.fullName = getFullName(customer);
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
    }

    private String getFullName(Customer customer) {
        return customer.getLastName().concat(", ").concat(customer.getFirstName());
    }
}
