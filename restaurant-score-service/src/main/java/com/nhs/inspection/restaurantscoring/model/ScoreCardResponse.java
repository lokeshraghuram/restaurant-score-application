package com.nhs.inspection.restaurantscoring.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.awt.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreCardResponse extends ScoreCard {
    private Point business_location;

    public Point getBusiness_location() {
        return business_location;
    }

    public void setBusiness_location(Point business_location) {
        this.business_location = business_location;
    }
}
