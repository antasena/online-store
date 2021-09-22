package com.example.onlinestore.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private Set<Address> address = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Orders> orders = new HashSet<>();
}
