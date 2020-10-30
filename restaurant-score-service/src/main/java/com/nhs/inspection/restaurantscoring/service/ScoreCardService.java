package com.nhs.inspection.restaurantscoring.service;

import com.nhs.inspection.restaurantscoring.model.ScoreCard;
import com.nhs.inspection.restaurantscoring.model.ScoreCardResponse;
import com.nhs.inspection.restaurantscoring.model.database.RestaurantData;
import com.nhs.inspection.restaurantscoring.repository.ScoreCardJdbcRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreCardService {

    private final ScoreCardJdbcRepository scoreCardJdbcRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ScoreCardService(ScoreCardJdbcRepository scoreCardJdbcRepository,
                            ModelMapper modelMapper) {
        this.scoreCardJdbcRepository = scoreCardJdbcRepository;
        this.modelMapper = modelMapper;
    }

    public List<ScoreCardResponse> getScoreCardsForBusinessId(String businessId) {
        List<RestaurantData> scoreCardsForInspectionId = scoreCardJdbcRepository.getScoreCardsForBusinessId(businessId);
        List<ScoreCardResponse> scoreCardResponses = new ArrayList<>();
        scoreCardsForInspectionId.forEach(restaurantData -> {
            ScoreCardResponse scoreCardResponse = modelMapper.map(restaurantData, ScoreCardResponse.class);
            scoreCardResponses.add(scoreCardResponse);
        });
        return scoreCardResponses;
    }

    public List<ScoreCardResponse> getScoreCardsForInspectionId(String inspectionId) {
        List<RestaurantData> scoreCardsForInspectionId = scoreCardJdbcRepository.getScoreCardsForInspectionId(inspectionId);
        List<ScoreCardResponse> scoreCardResponses = new ArrayList<>();
        scoreCardsForInspectionId.forEach(restaurantData -> {
            ScoreCardResponse scoreCardResponse = modelMapper.map(restaurantData, ScoreCardResponse.class);
            scoreCardResponses.add(scoreCardResponse);
        });
        return scoreCardResponses;
    }

    public ScoreCardResponse getScoreCardsForViolationId(String violationId) {
        RestaurantData scoreCardForViolationId = scoreCardJdbcRepository.getScoreCardForViolationId(violationId);
        return modelMapper.map(scoreCardForViolationId, ScoreCardResponse.class);
    }

    public void createScoreCard(ScoreCard scoreCard) {
        validateScoreCard(scoreCard);
        scoreCardJdbcRepository.createScoreCard(scoreCard);
    }

    private void validateScoreCard(ScoreCard scoreCard) {
        // Place holder for business logic or any validations
    }
}
