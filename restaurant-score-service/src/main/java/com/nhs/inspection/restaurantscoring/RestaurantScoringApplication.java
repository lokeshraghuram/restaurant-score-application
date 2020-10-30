package com.nhs.inspection.restaurantscoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantScoringApplication {

    Logger logger = LoggerFactory.getLogger(RestaurantScoringApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RestaurantScoringApplication.class, args);
    }

}
