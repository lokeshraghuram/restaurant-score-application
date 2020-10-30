package com.nhs.inspection.restaurantscoring.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Inspection implements Serializable {

    @NotNull
    private String inspection_id;
    private LocalDateTime inspection_date;
    private Double inspection_score;
    private String inspection_type;
    private List<Violation> violations;

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

    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }

    @Override
    public String toString() {
        return "Inspection{" +
                "inspection_id='" + inspection_id + '\'' +
                ", inspection_date=" + inspection_date +
                ", inspection_score=" + inspection_score +
                ", inspection_type='" + inspection_type + '\'' +
                ", violations=" + violations +
                '}';
    }
}
