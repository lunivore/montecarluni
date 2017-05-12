package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.Record
import java.time.LocalDateTime

class DistributionCalculator : ICalculateWeeklyDistribution {
    val neverUsed = LocalDateTime.of(1970, 1, 1, 0, 0)

    override fun calculateDistribution(completedDates: List<Record>): List<Int> {
        checkAllDatesValid(completedDates)
        val dateRange = findFirstAndLastCompletedDates(completedDates)

        val dateBrackets = calculateDateBrackets(dateRange)

        return dateBrackets.map {
            bracket ->
            completedDates.count {
                val date = it.getResolvedOrLastUpdatedDate()
                date != null &&
                with(date) {
                    (isAfter(bracket.first) &&
                    isBefore(bracket.second)) ||
                    isEqual(bracket.second)
                }
            }
        }
    }

    private fun calculateDateBrackets(dateRange : Pair<LocalDateTime, LocalDateTime>):
            MutableList<Pair<LocalDateTime, LocalDateTime>> {
        val dateBrackets = mutableListOf<Pair<LocalDateTime, LocalDateTime>>()
        var processedDate = dateRange.second

        while (processedDate != null &&
                processedDate.compareTo(dateRange.first) > 0) {

            var nextDate = processedDate.minusDays(7)
            dateBrackets.add(Pair(nextDate, processedDate))
            processedDate = nextDate
        }
        return dateBrackets
    }

    private fun findFirstAndLastCompletedDates(completedDates: List<Record>): Pair<LocalDateTime, LocalDateTime> {

        val earliestCompletedDate = completedDates.minBy { it.getResolvedOrLastUpdatedDate() ?: neverUsed }?.getResolvedOrLastUpdatedDate()
        val lastCompletedDate = completedDates.maxBy { it.getResolvedOrLastUpdatedDate() ?: neverUsed }?.getResolvedOrLastUpdatedDate()

        if (earliestCompletedDate == null || lastCompletedDate == null) {
            throw Exception("Could not find records to parse for date brackets. (Montecarluni should checked for this already!)")
        }

        return Pair(earliestCompletedDate, lastCompletedDate)
    }

    private fun checkAllDatesValid(completedDates: List<Record>) {
        if (completedDates.any {
            it.getResolvedOrLastUpdatedDate() == null
        }) {
            throw IllegalArgumentException("Montecarluni does not support tickets with no resolved or last updated date.")
        }
    }
}

