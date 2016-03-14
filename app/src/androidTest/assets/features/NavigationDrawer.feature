Feature: Navigation Drawer
  As a user
  I want to user the navigation drawer on the main screen
  so that I can quickly access others parts of the app

  Scenario: open navigation drawer
    Given I am on the main screen
    When I click on the navigation drawer button
    Then the navigation drawer is opened

  Scenario: list navigation drawer options
    Given I am on the main screen
    And the navigation drawer is opened
    Then all the menu options are properly listed
