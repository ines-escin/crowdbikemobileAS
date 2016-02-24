Feature: Occurrence
	As a user 
	I want to add, edit, remove, see and receive notifications about nearby occurrences 
	so that I can decide where I want to go

	Scenario: add new occurrence
		Given the system has no occurrence added at location "-8.055299","-34.951334"
		When I create a new occurrence at location "-8.055299","-34.951334"
		Then the occurrence is stored by the system

	Scenario: remove occurrence
		Given the system has an occurrence at location "-8.055299","-34.951334"
		When I remove the occurrence
		Then the occurrence is no longer stored in the system

	Scenario: nearby occurrence
		Given the system has an occurrence at location "-8.055299","-34.951334"
		When I am under 30 meters from an occurrence at location "-8.055299","-34.951334"
		Then a occurrence should be received
		
	Scenario: no nearby occurrence
		Given I am at location "-8.055299","-34.951334"
		And the system no occurrence near to 30 meters from ocation "-8.055299","-34.951334"
		Then no occurrence should be received

