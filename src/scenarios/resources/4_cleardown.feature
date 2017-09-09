Feature: Cleardown

Scenario: After a full forecast we want to clear everything

  Given "/example_files/closed_only_JIRA.csv" is imported
  And we chose a start date of 2017-04-30
  And I asked for a forecast for 100 work items using rows 4 onwards
  When I clear down the data
  Then all the relevant fields should be empty
