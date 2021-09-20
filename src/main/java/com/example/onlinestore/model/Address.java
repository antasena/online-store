package com.example.onlinestore.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    private String addressLine1;
    private String addressLine2;
    private String postcode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
