package com.nhs.inspection.restaurantscoring.repository;

import com.nhs.inspection.restaurantscoring.exception.CustomExceptionHandler;
import com.nhs.inspection.restaurantscoring.model.Restaurant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RestaurantStatusRepository extends BaseDatabaseRepository {

    private static final int YEAR_DAYS = 365;
    private final CustomExceptionHandler customExceptionHandler;

    public RestaurantStatusRepository(JdbcTemplate jdbcTemplate,
                                      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                      CustomExceptionHandler customExceptionHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.customExceptionHandler = customExceptionHandler;
    }

    public List<Restaurant> getInspectionDates() {
        try {
            List<Restaurant> restaurants = this.queryForList("SELECT business_id,inspection_date  FROM inspection.restaurant_data r1\n" +
                    "WHERE inspection_date = \n" +
                    "(SELECT MAX(inspection_date) FROM inspection.restaurant_data r2 WHERE r1.business_id = r2.business_id)\n" +
                    "GROUP BY business_id, inspection_date", new BeanPropertyRowMapper<Restaurant>(Restaurant.class));
            restaurants.forEach(restaurant -> {
                LocalDate lastInspectionDate = restaurant.getInspection_date().toLocalDate();
                LocalDate currentUtcDate = LocalDateTime.now(ZoneId.of("UTC")).toLocalDate();

                long between = ChronoUnit.DAYS.between(currentUtcDate, lastInspectionDate);
                if (between > YEAR_DAYS) {
                    restaurant.setStatus("Outdated");
                } else restaurant.setStatus("Active");
            });
            return restaurants;
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
        }
        return null;
    }

    public Restaurant getInspectionDate(String businessId) {
        try {
            Restaurant restaurant = this.queryForObject("SELECT business_id,inspection_date  FROM inspection.restaurant_data r1\n" +
                    "WHERE business_id = " +
                    "'" + businessId + "' and " +
                    "inspection_date = \n" +
                    "(SELECT MAX(inspection_date) FROM inspection.restaurant_data r2 WHERE r1.business_id = r2.business_id)\n" +
                    "GROUP BY business_id, inspection_date", new BeanPropertyRowMapper<Restaurant>(Restaurant.class));

            LocalDate lastInspectionDate = restaurant.getInspection_date().toLocalDate();
            LocalDate currentUtcDate = LocalDateTime.now(ZoneId.of("UTC")).toLocalDate();

            long between = ChronoUnit.DAYS.between(currentUtcDate, lastInspectionDate);
            if (between > YEAR_DAYS) {
                restaurant.setStatus("Outdated");
            } else restaurant.setStatus("Active");
            return restaurant;
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
        }
        return null;
    }

    public void updateRestaurantStatus(List<Restaurant> restaurants) {
        try {
            this.insertObject(prepareSqlUpdateFromRestaurants(), createNamedParameters(restaurants));
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
        }
    }

    public void updateRestaurantStatus(Restaurant restaurant) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("business_id", restaurant.getBusiness_id());
            params.put("status", restaurant.getStatus());
            params.put("inspection_date", restaurant.getInspection_date());
            namedParameterJdbcTemplate.update("update inspection.restaurant_status set inspection_date = :inspection_date " +
                    "and status = :status where business_id = :business_id", params);
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
        }
    }

    public void updateRestaurantStatus(String businessId, LocalDateTime inspectionDate, String status) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("business_id", businessId);
            params.put("inspection_date", inspectionDate);
            params.put("status", status);
            this.updateObject(prepareSqlUpdateFromRestaurants(), params);
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
        }
    }

    private List<Map<String, Object>> createNamedParameters(List<Restaurant> restaurants) {
        List<Map<String, Object>> paramsList = new ArrayList<>();

        restaurants.forEach(restaurant -> {
            Map<String, Object> params = new HashMap<>();
            params.put("business_id", restaurant.getBusiness_id());
            params.put("inspection_date", restaurant.getInspection_date());
            params.put("status", restaurant.getStatus());
            paramsList.add(params);
        });
        return paramsList;
    }

    private String prepareSqlUpdateFromRestaurants() {
        return "update inspection.restaurant_status SET " +
                "inspection_date=:inspection_date," +
                "status=:status" +
                " WHERE " +
                "business_id = :business_id";
    }

    public String getStatus(String businessId) {
        return this.queryForObject("select status from inspection.restaurant_status where business_id = ?", new Object[]{businessId},
                String.class);
    }
}
