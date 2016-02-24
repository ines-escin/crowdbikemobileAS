Feature: Tracking
	As a user
	I want to track, save and view my routes
	so that I can see all the information about my running such as time taken, distance, average speed and calories.

  	Scenario: start tracking service
	  Given I am at the MainActivity
	  When I click the start button
      Then the start button is changed to stop button
      And the tracking is started

    Scenario: stop tracking service
      Given I am at the MainActivity
      And the tracking service is already started
      When I click the stop button
      Then the tracking is stopped
      And I am at the ResultsActivity
      And I can see the distance, time taken and average speed

    Scenario: chronometer running while tracking service started
      Given I am at the MainActivity
      And  the tracking service is already started
      Then the chronometer is running

    Scenario: chronometer at zero
      Given I am at the MainActivity
      And  the tracking service is not started
      Then the chronometer is in 0