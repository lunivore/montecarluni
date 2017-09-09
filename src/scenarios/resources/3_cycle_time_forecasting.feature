Feature: Forecasting using cycle time

Scenario: Can load a typical JIRA csv and calculate the cycle times between tickets

  Given a typical JIRA export "/example_files/closed_only_JIRA.csv"
  When I import it into Montecarluni
  Then the cycle times should contain
"""
 | 2017-01-04 09:29 | -
 | 2017-01-06 16:50 | 2d 7h
 | 2017-01-11 16:09 | 4d 23h
 | 2017-01-16 14:39 | 4d 22h
"""