Feature: Sample Feature to test spaces feature

  #author : ABCD
  @TC-03 @smoke
  @dev
  Scenario: Sample Scenario to create spaces
    Given A User logs in with "shankar.gajangi@cognizant.com" and "password1"
    When user opens the spaces workobjects page
    And user navigates to new space wizard
    And creates a new space with name "MySpace"
    Then the new space should be successfully created