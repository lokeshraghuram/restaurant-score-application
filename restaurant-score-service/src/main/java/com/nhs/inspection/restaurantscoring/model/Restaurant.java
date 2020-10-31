package com.nhs.inspection.restaurantscoring.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Restaurant {
    @NotNull
    private String business_id;
    private LocalDateTime inspection_date;
    private String status;

    public Restaurant() {
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public LocalDateTime getInspection_date() {
        return inspection_date;
    }

    public void setInspection_date(LocalDateTime inspection_date) {
        this.inspection_date = inspection_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
