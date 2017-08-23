Feature: Forecasting

Scenario: Can make forecast using CSV file imported from JIRA

Given "/example_files/closed_only_JIRA.csv" is imported
When I ask for a forecast for the next 200 stories
Then I should see a percentage forecast
"""
100% | 2017-09-21
95% | 2017-08-24
90% | 2017-08-17
85% | 2017-08-10
80% | 2017-08-03
75% | 2017-08-03
70% | 2017-07-27
65% | 2017-07-27
60% | 2017-07-27
55% | 2017-07-20
50% | 2017-07-20
45% | 2017-07-20
40% | 2017-07-13
35% | 2017-07-13
30% | 2017-07-13
25% | 2017-07-06
20% | 2017-07-06
15% | 2017-06-29
10% | 2017-06-29
5% | 2017-06-22
0% | 2017-06-07
"""