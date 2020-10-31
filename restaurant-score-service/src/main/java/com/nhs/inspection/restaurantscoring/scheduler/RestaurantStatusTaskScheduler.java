package com.nhs.inspection.restaurantscoring.scheduler;

import com.nhs.inspection.restaurantscoring.service.RestaurantStatusService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * This scheduler acts as a back up process to keep the restaurant_status table in sync
 * with the restaurant_data table. If runs daily once
 */
@Configuration
@EnableScheduling
public class RestaurantStatusTaskScheduler {

    private final RestaurantStatusService restaurantStatusService;

    public RestaurantStatusTaskScheduler(RestaurantStatusService restaurantStatusService) {
        this.restaurantStatusService = restaurantStatusService;
    }

    /* Runs at 1am every day. Follows UTC timezone */
    @Scheduled(cron = "0 0 1 * * ?", zone = "UTC")
    public void scheduleFixedDelayTask() {
        restaurantStatusService.getInspectionDates();
    }
}