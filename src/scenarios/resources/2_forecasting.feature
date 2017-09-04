Feature: Forecasting

Scenario: Can make forecast using CSV file imported from JIRA

Given "/example_files/closed_only_JIRA.csv" is imported
When I ask for a forecast for the next 100 stories
Then I should see a percentage forecast
"""
100% | 2017-09-21
95% | 2017-08-24
90% | 2017-08-17
85% | 2017-08-10
80% | 2017-08-03
75% | 2017-08-03
70% | 2017-08-03
65% | 2017-07-27
60% | 2017-07-27
55% | 2017-07-20
50% | 2017-07-20
45% | 2017-07-20
40% | 2017-07-20
35% | 2017-07-13
30% | 2017-07-13
25% | 2017-07-13
20% | 2017-07-06
15% | 2017-07-06
10% | 2017-06-29
5% | 2017-06-22
0% | 2017-06-07
"""

Scenario: Can set a start date for the forecast

Given "/example_files/closed_only_JIRA.csv" is imported
When I ask for a forecast for 100 stories starting on 2017-06-01
Then I should see a percentage forecast
"""
100% | 2017-11-16
95% | 2017-10-19
90% | 2017-10-12
85% | 2017-10-05
80% | 2017-09-28
75% | 2017-09-28
70% | 2017-09-28
65% | 2017-09-21
60% | 2017-09-21
55% | 2017-09-14
50% | 2017-09-14
45% | 2017-09-14
40% | 2017-09-14
35% | 2017-09-07
30% | 2017-09-07
25% | 2017-09-07
20% | 2017-08-31
15% | 2017-08-31
10% | 2017-08-24
5% | 2017-08-17
0% | 2017-08-02
"""

Scenario: Can select a range to use for the forecast

Given "/example_files/closed_only_JIRA.csv" is imported
When I ask for a forecast for 100 stories using rows 4 onwards
Then I should see a percentage forecast
"""
100% | 2017-08-24
95% | 2017-07-27
90% | 2017-07-20
85% | 2017-07-20
80% | 2017-07-20
75% | 2017-07-13
70% | 2017-07-13
65% | 2017-07-13
60% | 2017-07-06
55% | 2017-07-06
50% | 2017-07-06
45% | 2017-07-06
40% | 2017-06-29
35% | 2017-06-29
30% | 2017-06-29
25% | 2017-06-29
20% | 2017-06-22
15% | 2017-06-22
10% | 2017-06-15
5% | 2017-06-15
0% | 2017-05-31
"""