package com.nhs.inspection.restaurantscoring.model.database;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.awt.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantData {
    private String business_id;
    private String business_name;
    private String business_address;
    private String business_city;
    private String business_state;
    private String business_postal_code;
    private Double business_latitude;
    private Double business_longitude;
    private String business_phone_number;
    private Point business_location;
    private String inspection_id;
    private LocalDateTime inspection_date;
    private Double inspection_score;
    private String inspection_type;
    private String violation_id;
    private String violation_description;
    private String risk_category;

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

    public String getInspection_id() {
        return inspection_id;
    }

    public void setInspection_id(String inspection_id) {
        this.inspection_id = inspection_id;
    }

    public LocalDateTime getInspection_date() {
        return inspection_date;
    }

    public void setInspection_date(LocalDateTime inspection_date) {
        this.inspection_date = inspection_date;
    }

    public Double getInspection_score() {
        return inspection_score;
    }

    public void setInspection_score(Double inspection_score) {
        this.inspection_score = inspection_score;
    }

    public String getInspection_type() {
        return inspection_type;
    }

    public void setInspection_type(String inspection_type) {
        this.inspection_type = inspection_type;
    }

    public String getViolation_id() {
        return violation_id;
    }

    public void setViolation_id(String violation_id) {
        this.violation_id = violation_id;
    }

    public String getViolation_description() {
        return violation_description;
    }

    public void setViolation_description(String violation_description) {
        this.violation_description = violation_description;
    }

    public String getRisk_category() {
        return risk_category;
    }

    public void setRisk_category(String risk_category) {
        this.risk_category = risk_category;
    }

    public RestaurantData() {
    }

    public Point getBusiness_location() {
        return business_location;
    }

    public void setBusiness_location(Point business_location) {
        this.business_location = business_location;
    }

}
