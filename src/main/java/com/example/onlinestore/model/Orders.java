package com.example.onlinestore.model;

import lombok.Data;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable=false, updatable=false)
    private Date orderDate;

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();
}
