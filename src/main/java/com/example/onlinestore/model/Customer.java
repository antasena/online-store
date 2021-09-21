package com.example.onlinestore.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @NotEmpty
    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private Set<Address> address = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Orders> orders = new HashSet<>();
}
