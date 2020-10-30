package com.nhs.inspection.restaurantscoring.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Violation implements Serializable {
    @NotNull
    private String violation_id;
    private String violation_description;
    private String risk_category;

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

    @Override
    public String toString() {
        return "Violation{" +
                "violation_id='" + violation_id + '\'' +
                ", violation_description='" + violation_description + '\'' +
                ", risk_category='" + risk_category + '\'' +
                '}';
    }
}
