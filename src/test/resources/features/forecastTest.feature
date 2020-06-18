	Feature: Sample feature to open forecast

  #author : ABCD
  @TC-02 @smoke
  Scenario: Sample Scenario to open forecast
    Given A User logs in with "tmason" and "Abcd@123"
    When user opens the forecast workobjects page
    And switches to close plans tab
    Then close plans view should be available