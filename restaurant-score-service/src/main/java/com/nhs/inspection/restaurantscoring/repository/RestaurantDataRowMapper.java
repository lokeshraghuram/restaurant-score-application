package com.nhs.inspection.restaurantscoring.repository;

import com.nhs.inspection.restaurantscoring.model.database.RestaurantData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class RestaurantDataRowMapper implements RowMapper<RestaurantData> {

    public static final Logger auditLogger = LoggerFactory.getLogger("restaurantDataRowMapper");

    @Override
    public RestaurantData mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return mapResultSet(rs);
        } catch (SQLException e) {
            auditLogger.warn("DATA_FILE_ROW_MAPPER_WARNING", e);
        }
        return null;
    }

    private RestaurantData mapResultSet(ResultSet rs) throws SQLException {
        RestaurantData restaurantData = new RestaurantData();
        restaurantData.setBusiness_id(rs.getString("business_id"));
        restaurantData.setBusiness_name(rs.getString("business_name"));
        restaurantData.setBusiness_address(rs.getString("business_address"));
        restaurantData.setBusiness_city(rs.getString("business_city"));
        restaurantData.setBusiness_state(rs.getString("business_state"));
        restaurantData.setBusiness_postal_code(rs.getString("business_postal_code"));
        restaurantData.setBusiness_latitude(rs.getDouble("business_latitude"));
        restaurantData.setBusiness_longitude(rs.getDouble("business_longitude"));

        String location = rs.getString("business_location");
        Point point = new Point();
        /* Create business_location Point from result */
        if (Objects.nonNull(location)) {
            String[] splitLocation = location.trim().split(",");
            point.setLocation(Double.parseDouble(splitLocation[0].substring(1)),
                    Double.parseDouble(splitLocation[1].trim().substring(0, splitLocation[1].length() - 1)));
            restaurantData.setBusiness_location(point);
        } else { /* Create business_location Point from latitude and longitude */
            double business_latitude = rs.getDouble("business_latitude");
            double business_longitude = rs.getDouble("business_longitude");
            point.setLocation(business_latitude, business_longitude);
            restaurantData.setBusiness_location(point);
        }

        restaurantData.setBusiness_phone_number(rs.getString("business_phone_number"));
        restaurantData.setInspection_id(rs.getString("inspection_id"));
        restaurantData.setInspection_date(rs.getTimestamp("inspection_date").toLocalDateTime());
        restaurantData.setInspection_score(rs.getDouble("inspection_score"));
        restaurantData.setInspection_type(rs.getString("inspection_type"));
        restaurantData.setViolation_id(rs.getString("violation_id"));
        restaurantData.setViolation_description(rs.getString("violation_description"));
        restaurantData.setRisk_category(rs.getString("risk_category"));

        return restaurantData;
    }
}
