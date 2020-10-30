package com.nhs.inspection.restaurantscoring.repository;

import com.nhs.inspection.restaurantscoring.exception.CustomExceptionHandler;
import com.nhs.inspection.restaurantscoring.model.ScoreCard;
import com.nhs.inspection.restaurantscoring.model.database.RestaurantData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

@Repository
public class ScoreCardJdbcRepository extends BaseDatabaseRepository {

    private final CustomExceptionHandler customExceptionHandler;
    private final RestaurantDataRowMapper restaurantDataRowMapper;

    public ScoreCardJdbcRepository(JdbcTemplate jdbcTemplate,
                                   NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                   CustomExceptionHandler customExceptionHandler,
                                   RestaurantDataRowMapper restaurantDataRowMapper) throws SQLException {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.customExceptionHandler = customExceptionHandler;
        this.restaurantDataRowMapper = restaurantDataRowMapper;
    }

    public List<RestaurantData> getScoreCardsForBusinessId(String businessId) {
        try {
            return this.queryForList("select * from inspection.restaurant_data where business_id = ?",
                    new Object[]{businessId},
                    restaurantDataRowMapper);
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
            return Collections.emptyList();
        }
    }

    public List<RestaurantData> getScoreCardsForInspectionId(String inspectionId) {
        try {
            return this.queryForList("select * from inspection.restaurant_data where inspection_id = ?",
                    new Object[]{inspectionId},
                    restaurantDataRowMapper);
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
            return Collections.emptyList();
        }
    }

    public RestaurantData getScoreCardForViolationId(String violationId) {
        try {
            return this.queryForObject("select * from inspection.restaurant_data where violation_id = ?",
                    new Object[]{violationId},
                    restaurantDataRowMapper);
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
            return null;
        }
    }

    public void createScoreCard(ScoreCard scoreCard){
        try {
            this.insertObject(prepareSqlInsertFromScoreCard(), createNamedParameters(scoreCard));
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
        }
    }

    private List<Map<String, Object>> createNamedParameters(ScoreCard scoreCard) {
        List<Map<String, Object>> paramsList = new ArrayList<>();

        scoreCard.getInspections().forEach(inspection -> {
            inspection.getViolations().forEach(violation -> {
                Map<String, Object> params = new HashMap<>();
                params.put("business_id", scoreCard.getBusiness_id());
                params.put("business_name", scoreCard.getBusiness_name());
                params.put("business_address", scoreCard.getBusiness_address());
                params.put("business_city", scoreCard.getBusiness_city());
                params.put("business_state", scoreCard.getBusiness_state());
                params.put("business_postal_code", scoreCard.getBusiness_postal_code());
                params.put("business_latitude", scoreCard.getBusiness_latitude());
                params.put("business_longitude", scoreCard.getBusiness_longitude());
                params.put("business_phone_number", scoreCard.getBusiness_phone_number());
                params.put("inspection_id", inspection.getInspection_id());
                params.put("inspection_type", inspection.getInspection_type());
                params.put("inspection_score", inspection.getInspection_score());
                params.put("inspection_date", inspection.getInspection_date());
                params.put("violation_id", violation.getViolation_id());
                params.put("violation_description", violation.getViolation_description());
                params.put("risk_category", violation.getRisk_category());
                paramsList.add(params);
            });
        });
        return paramsList;
    }

    private String prepareSqlInsertFromScoreCard() {
        String sql = "insert into inspection.restaurant_data" +
                "(business_id, business_name, business_address, business_city, business_state," +
                "business_postal_code, business_latitude, business_longitude, " +
                "business_phone_number, inspection_id, inspection_date, inspection_type, inspection_score," +
                "violation_id, violation_description, risk_category) values " +
                " (" +
                ":business_id, :business_name, :business_address, :business_city, :business_state, " +
                ":business_postal_code, :business_latitude, :business_longitude," +
                ":business_phone_number, :inspection_id, :inspection_date, :inspection_type, :inspection_score," +
                ":violation_id, :violation_description, :risk_category" +
                ")";
        return sql;
    }


}
