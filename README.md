Montecarluni - a Monte Carlo forecasting tool for JIRA

This tool will import an exported JIRA csv file containing tickets that you consider to be in a "Done" state. It will use the "resolved" dates from those tickets, or the "updated" date if no resolved date is found, to calculate the distribution of tickets closed each week.

From this, it will generate a Monte Carlo simulation; running through 500 scenarios in which your future stories are finished, and providing a distribution by percentage complete of the result.

The weekly distribution can also be pasted into Troy Magennis's "Throughput Forecaster", available in his spreadsheets at 
<a href="http://bit.ly/SimResources">Focused Objective Github.</a>

Please note that Montecarluni is a work in progress. The forecasts it generates are only as good as the data entered into them. Use the forecasts with care, and always look for multiple ways to forecast your project and compare the outcomes, especially if making a critical decision.

A good way to avoid needing forecasts like this is to limit investments and commitments, and keep your options open, so that no decision ends up being critical.

You are responsible for any decision you make using this tool!
