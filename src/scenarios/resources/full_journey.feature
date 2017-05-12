Feature: Full Journey

Scenario: Can load a typical JIRA csv and calculate the distribution from it

Given a typical JIRA export "/closed_only_JIRA.csv"
When I import it into Montecarluni
Then I should see the distribution
"""
6, 15, 3, 14, 2, 5, 6, 8, 5, 10, 15, 4, 2, 1
"""
When I copy it to the clipboard
Then I should be able to paste it somewhere else

