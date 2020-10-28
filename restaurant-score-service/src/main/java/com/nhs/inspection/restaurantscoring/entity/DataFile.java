package com.nhs.inspection.restaurantscoring.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "datafile")
public class DataFile {

    @Id
    private String business_id;

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public DataFile() {
    }
}
