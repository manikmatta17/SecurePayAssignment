Feature: Navigating the Secure Pay Website

Scenario: Contact Us page should not be submitted
	Given User is on Google
	When User Navigates to Contact us Page
	And Verify Contact Us page is Loaded
	And User enters the on Contact us form
	Then Form should not be submitted
