package com.example.onlinestore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @NotEmpty
    private String status;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @NotNull
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @NotNull
    @OneToOne
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();
}