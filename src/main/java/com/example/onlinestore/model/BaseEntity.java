package com.example.onlinestore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity {
    @Version
    private Long version;

    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date lastUpdatedDate;

    //1 Active, 0 Deleted
    @Column(nullable = false)
    private int recordStatus = 1;
}
