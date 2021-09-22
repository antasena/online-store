package com.example.onlinestore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity {
    @Version
    private Long version;

    @Column(insertable=false, updatable=false)
    private Date createdDate;

    @Column(insertable=false, updatable=false)
    private Date lastUpdatedDate;

    //1 Active, 0 Deleted
    @Column(nullable = false)
    private int recordStatus = 1;
}
