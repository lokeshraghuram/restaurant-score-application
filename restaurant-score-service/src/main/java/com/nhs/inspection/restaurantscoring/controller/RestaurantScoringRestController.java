package com.nhs.inspection.restaurantscoring.controller;

import com.nhs.inspection.restaurantscoring.exception.ErrorInfo;
import com.nhs.inspection.restaurantscoring.model.ScoreCard;
import com.nhs.inspection.restaurantscoring.model.ScoreCardResponse;
import com.nhs.inspection.restaurantscoring.service.RestaurantStatusService;
import com.nhs.inspection.restaurantscoring.service.ScoreCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurant-scoring")
public class RestaurantScoringRestController {

    private final ScoreCardService scoreCardService;
    private final RestaurantStatusService restaurantStatusService;

    public RestaurantScoringRestController(ScoreCardService scoreCardService, RestaurantStatusService restaurantStatusService) {
        this.scoreCardService = scoreCardService;
        this.restaurantStatusService = restaurantStatusService;
    }

    @Operation(summary = "Get status of restaurant - outdated/active")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant Inspection Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Record not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))})
    @GetMapping(value = "/status/business-id/{businessId}")
    @PreAuthorize("hasAnyRole('ROLE_INSPECTOR', 'ROLE_PUBLIC')")
    public String getStatusForBusinessId(@PathVariable("businessId") String businessId) {
        return restaurantStatusService.getRestaurantStatus(businessId);
    }

    @Operation(summary = "Get all inspection results of a business id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Inspection results",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScoreCardResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Record not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))})
    @GetMapping(value = "/scorecard/business-id/{businessId}")
    @PreAuthorize("hasAnyRole('ROLE_INSPECTOR', 'ROLE_PUBLIC')")
    public ScoreCardResponse getScoreCardForBusinessId(@PathVariable("businessId") String businessId) {
        return scoreCardService.getScoreCardsForBusinessId(businessId);
    }

    @Operation(summary = "Get all inspection results of a inspection id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Inspection results",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScoreCardResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Record not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))})
    @GetMapping(value = "/scorecard/inspection-id/{inspectionId}")
    @PreAuthorize("hasAnyRole('ROLE_INSPECTOR', 'ROLE_PUBLIC')")
    public List<ScoreCardResponse> getScoreCardForInspectionId(@PathVariable("inspectionId") String inspectionId) {
        return scoreCardService.getScoreCardsForInspectionId(inspectionId);
    }

    @Operation(summary = "Get details of a violation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Inspection results",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScoreCardResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Record not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))})
    @GetMapping(value = "/scorecard/violation-id/{violationId}")
    @PreAuthorize("hasAnyRole('ROLE_INSPECTOR', 'ROLE_PUBLIC')")
    public ScoreCardResponse getScoreCardForViolationId(@PathVariable("violationId") String violationId) {
        return scoreCardService.getScoreCardsForViolationId(violationId);
    }

    @Operation(summary = "Create score card with list of all violations after completing an inspection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add violations after an inspection",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Validation of score card has failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class))),
            @ApiResponse(responseCode = "500", description = "Database Related Errors/Primary Constraint Violation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))
    })
    @PostMapping(value = "/scorecard")
    @PreAuthorize("hasAuthority('score:write')")
    public String createScoreCard(@Valid @RequestBody ScoreCard scoreCard) {
        int recordsInserted = scoreCardService.createScoreCard(scoreCard);
        return "Score card is added to dataset successfully. "+ recordsInserted + " violations are added";
    }


    @Operation(summary = "Update details of score card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update violation details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Validation of score card has failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class))),
            @ApiResponse(responseCode = "500", description = "Database Related Errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))
    })
    @PutMapping(value = "/scorecard")
    @PreAuthorize("hasAuthority('score:write')")
    public String updateScoreCard(@Valid @RequestBody ScoreCard scoreCard) {
        int recordsUpdated = scoreCardService.updateScoreCard(scoreCard);
        return "Score card is updated successfully. "+ recordsUpdated + " violations are updated";
    }

    @Operation(summary = "Delete Violation from dataset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete violation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Database Related Errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))
    })
    @DeleteMapping(value = "/scorecard/violation-id/{violationId}")
    @PreAuthorize("hasAuthority('score:write')")
    public String deleteViolation(@PathVariable("violationId") String violationId) {
        int recordsDeleted = scoreCardService.deleteViolation(violationId);
        return String.valueOf(recordsDeleted) + "violation records are deleted";
    }

    @Operation(summary = "Delete all violations of an inspection from dataset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete violation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Database Related Errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class)))
    })
    @DeleteMapping(value = "/scorecard/inspection-id/{inspectionId}")
    @PreAuthorize("hasAuthority('score:write')")
    public String deleteInspection(@PathVariable("inspectionId") String inspectionId) {
        int recordsDeleted = scoreCardService.deleteInspection(inspectionId);
        return recordsDeleted + "violation records are deleted";
    }
}
