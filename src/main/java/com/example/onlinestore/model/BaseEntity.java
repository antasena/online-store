package com.example.onlinestore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity {
    @JsonIgnore
    @Version
    private Long version;

    @JsonIgnore
    private Date createdDate;

    @JsonIgnore
    private Date lastUpdatedDate;

    //1 Active, 0 Deleted
    @JsonIgnore
    private int recordStatus = 1;
}
