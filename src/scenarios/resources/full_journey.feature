Feature: Full Journey

Scenario: Can load a typical JIRA csv and calculate the distribution from it

Given a typical JIRA export "/closed_only_JIRA.csv"
When I import it into Montecarluni
Then I should see the distribution
"""
1, 2, 4, 15, 10, 5, 8, 6, 5, 2, 14, 3, 15, 6
"""
When I copy it to the clipboard
Then I should be able to paste it somewhere else

