Feature: To test feature of restaurant scoring service

  Scenario: S01-Test new score card creation after an inspection
    Given that for scenario "S01" inspection is done and score card is available
    When request is sent to restaurant scoring service
    Then response of scenario "S01" must have 200 status code
    And response of scenario "S01" must match with the expected result
    When we get the score card of the same business id, response must have all fields mapped