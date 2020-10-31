package com.nhs.inspection.restaurantscoring.service;

import com.nhs.inspection.restaurantscoring.exception.CustomExceptionHandler;
import com.nhs.inspection.restaurantscoring.model.*;
import com.nhs.inspection.restaurantscoring.model.database.RestaurantData;
import com.nhs.inspection.restaurantscoring.repository.ScoreCardJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreCardService {

    private final ScoreCardJdbcRepository scoreCardJdbcRepository;
    private final RestaurantStatusService restaurantStatusService;
    private final CustomExceptionHandler customExceptionHandler;

    @Autowired
    public ScoreCardService(ScoreCardJdbcRepository scoreCardJdbcRepository,
                            RestaurantStatusService restaurantStatusService, CustomExceptionHandler customExceptionHandler) {
        this.scoreCardJdbcRepository = scoreCardJdbcRepository;
        this.restaurantStatusService = restaurantStatusService;
        this.customExceptionHandler = customExceptionHandler;
    }

    /** Fetches the violations for business id from public dataset and converts to ScoreCardResponse object
     * @param businessId Business Id
     * @return Violation Report from public dataset
     */
    public ScoreCardResponse getScoreCardsForBusinessId(String businessId) {
        List<RestaurantData> scoreCardsForBusinessId = scoreCardJdbcRepository.getScoreCardsForBusinessId(businessId);

        if (!CollectionUtils.isEmpty(scoreCardsForBusinessId)) {
            ScoreCardResponse scoreCardResponse = new ScoreCardResponse();
            scoreCardResponse.setBusiness_id(scoreCardsForBusinessId.get(0).getBusiness_id());
            scoreCardResponse.setBusiness_name(scoreCardsForBusinessId.get(0).getBusiness_name());
            scoreCardResponse.setBusiness_address(scoreCardsForBusinessId.get(0).getBusiness_address());
            scoreCardResponse.setBusiness_city(scoreCardsForBusinessId.get(0).getBusiness_city());
            scoreCardResponse.setBusiness_state(scoreCardsForBusinessId.get(0).getBusiness_state());
            scoreCardResponse.setBusiness_postal_code(scoreCardsForBusinessId.get(0).getBusiness_postal_code());
            scoreCardResponse.setBusiness_latitude(scoreCardsForBusinessId.get(0).getBusiness_latitude());
            scoreCardResponse.setBusiness_longitude(scoreCardsForBusinessId.get(0).getBusiness_longitude());
            scoreCardResponse.setBusiness_location(scoreCardsForBusinessId.get(0).getBusiness_location());
            scoreCardResponse.setBusiness_phone_number(scoreCardsForBusinessId.get(0).getBusiness_phone_number());
            /* Group by Inspection Id */
            Map<String, List<RestaurantData>> groupedScoreCards = scoreCardsForBusinessId.stream()
                    .collect(Collectors.groupingBy(RestaurantData::getInspection_id));

            List<Inspection> inspections = new ArrayList<>();
            groupedScoreCards.forEach((inspectionId, scoreCardsForInspectionId) -> {
                Inspection inspection = new Inspection();

                inspection.setInspection_id(scoreCardsForInspectionId.get(0).getInspection_id());
                inspection.setInspection_type(scoreCardsForInspectionId.get(0).getInspection_id());
                inspection.setInspection_id(scoreCardsForInspectionId.get(0).getInspection_type());
                inspection.setInspection_date(scoreCardsForInspectionId.get(0).getInspection_date());
                inspection.setInspection_score(scoreCardsForInspectionId.get(0).getInspection_score());

                List<Violation> violations = new ArrayList<>();
                scoreCardsForInspectionId.forEach(restaurantData -> {
                    Violation violation = new Violation();
                    violation.setViolation_id(restaurantData.getViolation_id());
                    violation.setViolation_description(restaurantData.getViolation_description());
                    violation.setRisk_category(restaurantData.getRisk_category());
                    violations.add(violation);
                });

                inspection.setViolations(violations);
                inspections.add(inspection);
            });
            scoreCardResponse.setInspectionList(inspections);
            return scoreCardResponse;
        } else
            customExceptionHandler.throwInternalException("RECORD_NOT_FOUND", "No records found for this business id", HttpStatus.BAD_REQUEST);
        return null;
    }

    private ScoreCardResponse createScoreCardResponseFromRestaurantData(RestaurantData restaurantData) {
        ScoreCardResponse scoreCardResponse = new ScoreCardResponse();

        scoreCardResponse.setBusiness_id(restaurantData.getBusiness_id());
        scoreCardResponse.setBusiness_name(restaurantData.getBusiness_name());
        scoreCardResponse.setBusiness_address(restaurantData.getBusiness_address());
        scoreCardResponse.setBusiness_city(restaurantData.getBusiness_city());
        scoreCardResponse.setBusiness_state(restaurantData.getBusiness_state());
        scoreCardResponse.setBusiness_postal_code(restaurantData.getBusiness_postal_code());
        scoreCardResponse.setBusiness_latitude(restaurantData.getBusiness_latitude());
        scoreCardResponse.setBusiness_longitude(restaurantData.getBusiness_longitude());
        scoreCardResponse.setBusiness_location(restaurantData.getBusiness_location());
        scoreCardResponse.setBusiness_phone_number(restaurantData.getBusiness_phone_number());
        Inspection inspection = new Inspection();
        inspection.setInspection_id(restaurantData.getInspection_id());
        inspection.setInspection_type(restaurantData.getInspection_type());
        inspection.setInspection_date(restaurantData.getInspection_date());
        inspection.setInspection_score(restaurantData.getInspection_score());
        Violation violation = new Violation();
        violation.setViolation_id(restaurantData.getViolation_id());
        violation.setViolation_description(restaurantData.getViolation_description());
        violation.setRisk_category(restaurantData.getRisk_category());
        inspection.setViolations(Collections.singletonList(violation));
        scoreCardResponse.setInspectionList(Collections.singletonList(inspection));

        return scoreCardResponse;
    }

    /** Fetches the violations for inspection id from public dataset and converts to List of ScoreCardResponse objects
     * @param inspectionId Inspection Id
     * @return Violation Reports from public dataset
     */
    public List<ScoreCardResponse> getScoreCardsForInspectionId(String inspectionId) {
        List<RestaurantData> scoreCardsForInspectionId = scoreCardJdbcRepository.getScoreCardsForInspectionId(inspectionId);
        List<ScoreCardResponse> scoreCardResponses = new ArrayList<>();
        scoreCardsForInspectionId.forEach(restaurantData -> {
            ScoreCardResponse scoreCardResponse = createScoreCardResponseFromRestaurantData(restaurantData);
            scoreCardResponses.add(scoreCardResponse);
        });
        return scoreCardResponses;
    }

    /** Fetches the violation for id from public dataset and converts to ScoreCardResponse object
     * @param violationId Violation Id
     * @return Violation Reports from public dataset
     */
    public ScoreCardResponse getScoreCardsForViolationId(String violationId) {
        RestaurantData scoreCardForViolationId = scoreCardJdbcRepository.getScoreCardForViolationId(violationId);
        return createScoreCardResponseFromRestaurantData(scoreCardForViolationId);
    }

    /** Creates violations in public dataset from the score card published by inspection officer
     * Update restaurant status asynchronously by using the business id and inspection date that is passed in the request(score card)
     * @param scoreCard ScoreCard
     * @return Number of violations added to public dataset
     */
    public int createScoreCard(ScoreCard scoreCard) {
        validateScoreCard(scoreCard);
        int recordsInserted = scoreCardJdbcRepository.createScoreCard(scoreCard);
        LocalDateTime inspectionDate = getInspectionDate(scoreCard);
        /* Async processing. Child thread will be created at this point. Parent thread will continue its processing
        without waiting for child thread's completion */
        restaurantStatusService.updateRestaurantStatus(scoreCard.getBusiness_id(), inspectionDate);
        return recordsInserted;
    }

    private LocalDateTime getInspectionDate(ScoreCard scoreCard) {
        if(Objects.nonNull(scoreCard.getInspections().get(0).getInspection_date()))
            return scoreCard.getInspections().get(0).getInspection_date();
        else return LocalDateTime.now(ZoneId.of("UTC"));
    }

    /** Update violations in public dataset from the score card published by inspection officer
     * Update restaurant status asynchronously by using the business id and inspection date that is passed in the request(score card)
     * @param scoreCard ScoreCard
     * @return Number of violations updated in public dataset
     */
    public int updateScoreCard(ScoreCard scoreCard) {
        validateScoreCard(scoreCard);
        int recordsUpdated = scoreCardJdbcRepository.updateScoreCard(scoreCard);
        LocalDateTime inspectionDate = getInspectionDate(scoreCard);
        /* Async processing. Child thread will be created at this point. Parent thread will continue its processing
        without waiting for child thread's completion */
        restaurantStatusService.updateRestaurantStatus(scoreCard.getBusiness_id(), inspectionDate);
        return recordsUpdated;
    }

    /** Delete violation in public dataset that matches the violationId passed in the request
     * Update restaurant status asynchronously by using the business id of the violation that is passed in the request
     * Get the latest inspection_date after deleting the violation, check its age and update the status
     * @param violationId Violation Id
     * @return Number of violations deleted in public dataset
     */
    public int deleteViolation(String violationId) {
        Map<String, String> queryResultMap = scoreCardJdbcRepository.deleteViolationScoreCard(violationId);
        /* Async processing. Child thread will be created at this point. Parent thread will continue its processing
        without waiting for child thread's completion */
        restaurantStatusService.updateRestaurantStatus(queryResultMap.get("businessId"));
        return Integer.parseInt(queryResultMap.get("deletedEntries"));
    }

    /** Delete violation in public dataset that matches the inspectionId passed in the request
     * @param inspectionId Inspection Id
     * @return Number of violations deleted in public dataset
     */
    public int deleteInspection(String inspectionId) {
        return scoreCardJdbcRepository.deleteInspectionScoreCards(inspectionId);
    }

    private void validateScoreCard(ScoreCard scoreCard) {
        // Place holder for business logic or any validations
    }
}
