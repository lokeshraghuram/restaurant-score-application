package com.nhs.inspection.restaurantscoring;

import com.nhs.inspection.restaurantscoring.repository.FilesJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantScoringApplication {

    @Autowired
    private FilesJpaRepository filesJpaRepository;

    Logger logger = LoggerFactory.getLogger(RestaurantScoringApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RestaurantScoringApplication.class, args);
    }

}
