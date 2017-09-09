Feature: Full Journey

Scenario: Can load a typical JIRA csv and calculate the distribution of closed tickets per week from it

Given a typical JIRA export "/example_files/closed_only_JIRA.csv"
When I import it into Montecarluni
Then I should see the distribution
"""
from 2016-12-30 to 2017-01-05 | 1
from 2017-01-06 to 2017-01-12 | 2
from 2017-01-13 to 2017-01-19 | 4
from 2017-01-20 to 2017-01-26 | 16
from 2017-01-27 to 2017-02-02 | 9
from 2017-02-03 to 2017-02-09 | 6
from 2017-02-10 to 2017-02-16 | 7
from 2017-02-17 to 2017-02-23 | 7
from 2017-02-24 to 2017-03-02 | 4
from 2017-03-03 to 2017-03-09 | 3
from 2017-03-10 to 2017-03-16 | 13
from 2017-03-17 to 2017-03-23 | 3
from 2017-03-24 to 2017-03-30 | 15
from 2017-03-31 to 2017-04-06 | 6
"""

Scenario: Can copy the distribution to the clipboard

Given "/example_files/closed_only_JIRA.csv" is imported
When I copy it to the clipboard
Then pasting it elsewhere should result in
"""
1, 2, 4, 16, 9, 6, 7, 7, 4, 3, 13, 3, 15, 6
"""

Scenario: Can copy selection to the clipboard

Given "/example_files/closed_only_JIRA.csv" is imported
When I copy rows 4 onwards to the clipboard
Then pasting it elsewhere should result in
"""
16, 9, 6, 7, 7, 4, 3, 13, 3, 15, 6
"""