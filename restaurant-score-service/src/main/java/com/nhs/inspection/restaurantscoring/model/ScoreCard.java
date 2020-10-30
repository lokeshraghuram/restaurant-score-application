package com.nhs.inspection.restaurantscoring.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreCard implements Serializable {
    @NotNull
    protected String business_id;
    protected String business_name;
    protected String business_address;
    protected String business_city;
    protected String business_state;
    protected String business_postal_code;
    protected Double business_latitude;
    protected Double business_longitude;
    protected String business_phone_number;
    protected List<Inspection> inspections;

    public ScoreCard() {
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

    public String getBusiness_city() {
        return business_city;
    }

    public void setBusiness_city(String business_city) {
        this.business_city = business_city;
    }

    public String getBusiness_state() {
        return business_state;
    }

    public void setBusiness_state(String business_state) {
        this.business_state = business_state;
    }

    public String getBusiness_postal_code() {
        return business_postal_code;
    }

    public void setBusiness_postal_code(String business_postal_code) {
        this.business_postal_code = business_postal_code;
    }

    public Double getBusiness_latitude() {
        return business_latitude;
    }

    public void setBusiness_latitude(Double business_latitude) {
        this.business_latitude = business_latitude;
    }

    public Double getBusiness_longitude() {
        return business_longitude;
    }

    public void setBusiness_longitude(Double business_longitude) {
        this.business_longitude = business_longitude;
    }

    public String getBusiness_phone_number() {
        return business_phone_number;
    }

    public void setBusiness_phone_number(String business_phone_number) {
        this.business_phone_number = business_phone_number;
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    public void setInspectionList(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    @Override
    public String toString() {
        return "ScoreCard{" +
                "business_id='" + business_id + '\'' +
                ", business_name='" + business_name + '\'' +
                ", business_address='" + business_address + '\'' +
                ", business_city='" + business_city + '\'' +
                ", business_state='" + business_state + '\'' +
                ", business_postal_code='" + business_postal_code + '\'' +
                ", business_latitude=" + business_latitude +
                ", business_longitude=" + business_longitude +
                ", business_phone_number='" + business_phone_number + '\'' +
                ", inspections=" + inspections +
                '}';
    }
}
