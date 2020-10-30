package com.nhs.inspection.restaurantscoring.controller;

import com.nhs.inspection.restaurantscoring.exception.ErrorInfo;
import com.nhs.inspection.restaurantscoring.model.ScoreCard;
import com.nhs.inspection.restaurantscoring.model.ScoreCardResponse;
import com.nhs.inspection.restaurantscoring.service.ScoreCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/restaurant-scoring")
public class RestaurantScoringRestController {

    ScoreCardService scoreCardService;

    public RestaurantScoringRestController(ScoreCardService scoreCardService) {
        this.scoreCardService = scoreCardService;
    }

    @Operation(summary = "Get all inspection results of given business id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Inspection results",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScoreCardResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Record not found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorInfo.class)))})
    @GetMapping(value = "/scorecard/business-id/{business_id}")
    public ScoreCardResponse getScoreCardForBusinessId(@PathVariable("business_id") String business_id) {
        return scoreCardService.getScoreCardsForBusinessId(business_id);
    }

    @Operation(summary = "Get all inspection results of given inspection id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Inspection results",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScoreCardResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Record not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))})
    @GetMapping(value = "/scorecard/inspection-id/{inspection_id}")
    public List<ScoreCardResponse> getScoreCardForInspectionId(@PathVariable("inspection_id") String inspection_id) {
        return scoreCardService.getScoreCardsForInspectionId(inspection_id);
    }

    @Operation(summary = "Get full details of a particular violation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Inspection results",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScoreCardResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Record not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))})
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
