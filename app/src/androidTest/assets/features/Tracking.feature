Feature: Tracking
	As a user
	I want to track, save and view my routes
	so that I can see all the information about my running such as time taken, distance, average speed and calories.

  	Scenario: start tracking
	  Given I am on the main screen
	  When I click to start tracking
      Then the tracking is started
      And the chronometer starts counting

    Scenario: stop tracking
      Given I am on the main screen
      And the tracking is started
      When I click to stop tracking
      Then the tracking is stopped
      And I should be on the results screen
      And I can see the tracking details

    Scenario: show current path on map
      Given I just started a new tracking
      When I go to a new location
      Then I should see a line of my path on map

    Scenario: list saved tracks
      Given I have two tracks stored
      When I go to the history screen
      Then my tracks are displayed on the list

    Scenario: show saved track
      Given I have two tracks stored
      When I go to the history screen
      And I click on a track
      Then I can see the tracking details


