package com.nhs.inspection.restaurantscoring.repository;

import com.nhs.inspection.restaurantscoring.exception.CustomExceptionHandler;
import com.nhs.inspection.restaurantscoring.model.Inspection;
import com.nhs.inspection.restaurantscoring.model.ScoreCard;
import com.nhs.inspection.restaurantscoring.model.database.RestaurantData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Repository
public class ScoreCardJdbcRepository extends BaseDatabaseRepository {

    private final CustomExceptionHandler customExceptionHandler;
    private final RestaurantDataRowMapper restaurantDataRowMapper;

    public ScoreCardJdbcRepository(JdbcTemplate jdbcTemplate,
                                   NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                   CustomExceptionHandler customExceptionHandler,
                                   RestaurantDataRowMapper restaurantDataRowMapper) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.customExceptionHandler = customExceptionHandler;
        this.restaurantDataRowMapper = restaurantDataRowMapper;
    }

    /**
     * @param businessId Business Id
     * @return List of Restaurant Data objects from public data set
     */
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

    /**
     * @param inspectionId Inspection Id
     * @return List of Restaurant Data objects from public data set
     */
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

    /**
     * @param violationId Violation Id
     * @return List of Restaurant Data objects from public data set
     */
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

    /**
     * @param scoreCard ScoreCard
     * @return Number of effected entries in database
     */
    public int createScoreCard(ScoreCard scoreCard) {
        try {
            return Arrays.stream(this.insertObject(prepareSqlInsertFromScoreCard(), createNamedParameters(scoreCard))).sum();
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
        }
        return 0;
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
                params.put("inspection_date", getInspectionDate(inspection));
                params.put("violation_id", violation.getViolation_id());
                params.put("violation_description", violation.getViolation_description());
                params.put("risk_category", violation.getRisk_category());
                paramsList.add(params);
            });
        });
        return paramsList;
    }

    private LocalDateTime getInspectionDate(Inspection inspection) {
        if (Objects.nonNull(inspection.getInspection_date()))
            return inspection.getInspection_date();
        else return LocalDateTime.now(ZoneId.of("UTC"));
    }

    private String prepareSqlInsertFromScoreCard() {
        return "insert into inspection.restaurant_data" +
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
    }

    private String prepareSqlUpdateFromScoreCard() {
        return "update inspection.restaurant_data SET " +
                "business_name=:business_name, business_address=:business_address, business_city=:business_city," +
                "business_state=:business_state, business_postal_code=:business_postal_code, business_latitude=:business_latitude," +
                "business_longitude=:business_longitude, business_phone_number=:business_phone_number, " +
                "inspection_date=:inspection_date," +
                "inspection_type=:inspection_type, inspection_score=:inspection_score, " +
                "violation_description=:violation_description," +
                "risk_category=:risk_category" +
                " WHERE " +
                "(business_id = :business_id and inspection_id = :inspection_id and violation_id = :violation_id)";
    }


    /**
     * @param scoreCard ScoreCard
     * @return Number of effected entries in database
     */
    public int updateScoreCard(ScoreCard scoreCard) {
        try {
            return Arrays.stream(this.updateObject(prepareSqlUpdateFromScoreCard(), createNamedParameters(scoreCard))).sum();
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
        }
        return 0;
    }

    /**
     * @param violationId Violation Id
     * @return Number of effected entries in database
     */
    public Map<String, String> deleteViolationScoreCard(String violationId) {
        try {
            Map<String, String> queryResult = new HashMap<>();
            int deletedEntries = this.deleteObject("delete from inspection.restaurant_data where violation_id = ?",
                    new Object[]{violationId});
            queryResult.put("businessId", getScoreCardForViolationId(violationId).getBusiness_id());
            queryResult.put("deletedRows", String.valueOf(deletedEntries));
            return queryResult;

        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
            return Collections.emptyMap();
        }
    }

    /**
     * @param inspectionId Inspection Id
     * @return Number of effected entries in database
     */
    public int deleteInspectionScoreCards(String inspectionId) {
        try {
            return this.deleteObject("delete from inspection.restaurant_data where inspection_id = ?",
                    new Object[]{inspectionId});
        } catch (Exception ex) {
            customExceptionHandler.handleSqlRepositoryExceptions(ex);
            return 0;
        }
    }
}
