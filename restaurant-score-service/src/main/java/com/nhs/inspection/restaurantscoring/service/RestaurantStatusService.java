package com.nhs.inspection.restaurantscoring.service;

import com.nhs.inspection.restaurantscoring.model.Restaurant;
import com.nhs.inspection.restaurantscoring.model.ScoreCardResponse;
import com.nhs.inspection.restaurantscoring.repository.RestaurantStatusRepository;
import com.nhs.inspection.restaurantscoring.repository.ScoreCardJdbcRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantStatusService {

    private final RestaurantStatusRepository restaurantStatusRepository;

    public RestaurantStatusService(RestaurantStatusRepository restaurantStatusRepository) {
        this.restaurantStatusRepository = restaurantStatusRepository;
    }

    @Async
    public void updateRestaurantStatus(String businessId, LocalDateTime inspectionDate){
        restaurantStatusRepository.updateRestaurantStatus(businessId, inspectionDate, "Active");
    }

    /** Gets latest inspection date of each business id, checks if it is older than 365 days
     * If yes, status will be updated to "Obsolete". Else status will be updated to "Active"
     */
    public void getInspectionDates() {
        List<Restaurant> restaurants = restaurantStatusRepository.getInspectionDates();
        restaurantStatusRepository.updateRestaurantStatus(restaurants);
    }

    public void updateRestaurantStatus(String businessId) {
        Restaurant restaurant = restaurantStatusRepository.getInspectionDate(businessId);
        restaurantStatusRepository.updateRestaurantStatus(restaurant);
    }

    public String getRestaurantStatus(String businessId) {
        return restaurantStatusRepository.getStatus(businessId);
    }
}
