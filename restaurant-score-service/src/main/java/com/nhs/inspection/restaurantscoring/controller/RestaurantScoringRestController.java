package com.nhs.inspection.restaurantscoring.controller;

import com.nhs.inspection.restaurantscoring.model.ScoreCard;
import com.nhs.inspection.restaurantscoring.model.ScoreCardResponse;
import com.nhs.inspection.restaurantscoring.service.ScoreCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurant-scoring")
public class RestaurantScoringRestController {

    ScoreCardService scoreCardService;

    public RestaurantScoringRestController(ScoreCardService scoreCardService) {
        this.scoreCardService = scoreCardService;
    }

    @GetMapping(value = "/scorecard/business-id/{business_id}")
    public List<ScoreCardResponse> getScoreCardForBusinessId(@PathVariable("business_id") String business_id) {
        return scoreCardService.getScoreCardsForBusinessId(business_id);
    }

    @GetMapping(value = "/scorecard/inspection-id/{inspection_id}")
    public List<ScoreCardResponse> getScoreCardForInspectionId(@PathVariable("inspection_id") String inspection_id) {
        return scoreCardService.getScoreCardsForInspectionId(inspection_id);
    }

    @GetMapping(value = "/scorecard/violation-id/{violation_id}")
    public ScoreCardResponse getScoreCardForViolationId(@PathVariable("violation_id") String violationId) {
        return scoreCardService.getScoreCardsForViolationId(violationId);
    }

    @PostMapping(value = "/scorecard")
    public String createScoreCard(@Valid @RequestBody ScoreCard scoreCard) {
        scoreCardService.createScoreCard(scoreCard);
        return "Score card is added to dataset successfully";
    }
}
