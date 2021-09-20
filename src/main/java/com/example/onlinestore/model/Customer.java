package com.example.onlinestore.model;

import java.util.Set;

public class Customer {
    private Long id;
    private String firstName;
    private String lasName;
    private String email;
    private String phoneNumber;
    private Set<Address> address;
}
