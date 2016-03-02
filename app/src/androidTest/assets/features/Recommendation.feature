Feature: Route recommendation
	As a user 
	I want to see the indicated routes 
	so that I can decide where I want to go

  Scenario: most comfortable routes
	Given: I choose a place to go
	And there are comfortable routes to go through
	Then I should see a comfortable route recommendation in the recommendation list

  Scenario: most used routes
    Given: I choose a place to go
    And there are routes used by most users to go through
    Then I should see the most used routes in the recommendation list

  Scenario: routes with less or no bus on the track
    Given: I choose a place to go
    And there are routes with less or no bus on the track to go through
    Then I should see routes with less or no bus on the track in the recommendation list

  Scenario: routes with damaged roads
    Given: I choose a place to go
    And there are routes with damaged roads to go through
    Then I should see routes with damaged roads in the warning list

  Scenario: routes with slow traffic roads
    Given: I choose a place to go
    And there are routes with slow traffic roads to go through
    Then I should see routes with slow traffic roads in the warning list




