package com.nhs.inspection.bdd.stepdefs;

import com.nhs.inspection.bdd.component.BagJdbcTemplate;
import com.nhs.inspection.bdd.component.LoadProperties;
import com.nhs.inspection.bdd.helper.TestHelper;
import com.nhs.inspection.service.RestAssuredService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FeatureExecution extends RestAssuredService {

    private final TestHelper testHelper = new TestHelper();
    private Response response;
    private String requestBody;
    private String featureNumber;
    private String scenarioNumber;

    @Autowired
    private LoadProperties loadProperties;

    @Autowired
    private BagJdbcTemplate bagJdbcTemplate;

    @Override
    protected String givenStep() {
        requestBody = testHelper.readTextFileAsString(featureNumber, scenarioNumber, "request.json");
        return requestBody;
    }

    @Given("that for scenario {string} inspection is done and score card is available")
    public void thatForScenarioInspectionIsDoneAndScoreCardIsAvailable(String scenarioNumber) throws IOException {
        prepareRequest(scenarioNumber);
    }

    @Then("response of scenario {string} must have {int} status code")
    public void responseOfScenarioMustHaveStatuscode(String scenarioCode, int statusCode) throws IOException {
        this.thenStep(response, statusCode);
    }

    @And("response of scenario {string} must match with the expected result")
    public void responseOfScenarioMustMatchWithTheExpectedResult(String scenarioCode) throws IOException {
        validateResponse(this.response, this.featureNumber, this.scenarioNumber);
    }

    private void validateResponse(Response response, String featureNUmber, String scenarioNumber) throws IOException {
        String expectedResult = testHelper.readFileFromSourceAsString(featureNumber, scenarioNumber, "response.json");
        Assert.assertEquals(expectedResult, response.getBody().asString());
    }

    @Override
    protected void thenStep(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    private void prepareRequest(String scenarioNumber) throws IOException {
        assignFeatureAndSecenario(scenarioNumber);
        this.givenStep();
    }

    public void assignFeatureAndSecenario(String scenarioNumber) {
        this.featureNumber = "F1";
        this.scenarioNumber = scenarioNumber;
    }

    @Override
    protected void thenStep(Response response) {

    }

    @When("request is sent to restaurant scoring service")
    public void requestIsSentToRestaurantScoringService() {
        response = requestIsFired();
    }

    private Response requestIsFired() {

        return whenStep(testHelper.createHeaderForInspector(), requestBody, "http://localhost:8082/restaurant-scoring/scorecard",
                "POST",
                "application/json",
                null);
    }


    @When("we get the score card of the same business id, response must have all fields mapped")
    public void weGetTheScoreCardOfTheSameBusinessIdResponseMustHaveAllFieldsMapped() throws JSONException {
        Response response = whenStep(testHelper.createHeaderForPublic(), requestBody, "http://localhost:8082/restaurant-scoring/scorecard/business-id/b3",
                "GET",
                "application/json",
                null);
        String expectedResult = testHelper.readFileFromSourceAsString(featureNumber, scenarioNumber, "response1.json");
        JSONAssert.assertEquals("JSON Properties doesn't Match", expectedResult, response.getBody().asString(), JSONCompareMode.NON_EXTENSIBLE);
    }
}
