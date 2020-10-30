package com.nhs.inspection.restaurantscoring.repository;

import com.nhs.inspection.restaurantscoring.exception.CustomExceptionHandler;
import com.nhs.inspection.restaurantscoring.model.ScoreCard;
import com.nhs.inspection.restaurantscoring.model.database.RestaurantData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

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
                params.put("inspection_id", inspection.getInspection_id());
                params.put("violation_id", violation.getViolation_id());
                paramsList.add(params);
            });
        });
        return paramsList;
    }

    private String prepareSqlInsertFromScoreCard() {
        String sql = "insert into inspection.restaurant_data(business_id, business_name, inspection_id, violation_id) values " +
                " (" +
                ":business_id, :business_name, :inspection_id, :violation_id" +
                ")";
        //System.out.println("SQL->"+sql);
        return sql;
    }


}
