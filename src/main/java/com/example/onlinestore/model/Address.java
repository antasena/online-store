package com.example.onlinestore.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column()
    private String addressLine1;
    @Column(nullable = false)
    private String addressLine2;
    @Column(length = 30)
    private String postcode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
