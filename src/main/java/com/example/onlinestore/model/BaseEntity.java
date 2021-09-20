package com.example.onlinestore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity {
    @Version
    private Long version;

    private Date createdDate;
    private Date lastUpdatedDate;
}
