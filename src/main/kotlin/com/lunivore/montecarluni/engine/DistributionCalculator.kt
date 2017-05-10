package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.Record
import java.time.LocalDateTime

class DistributionCalculator : ICalculateWeeklyDistribution {
    override fun calculateDistribution(completedDates: List<Record>): List<Int> {
        val lastCompletedDate = completedDates.maxBy {it.resolvedDate}
        val earliestCompletedDate = completedDates.minBy {it.resolvedDate}

        val dateBrackets = mutableListOf<Pair<LocalDateTime, LocalDateTime>>()
        var processedDate = lastCompletedDate?.resolvedDate

        while (processedDate != null &&
                processedDate.compareTo(earliestCompletedDate?.resolvedDate) > 0) {

            var nextDate = processedDate.minusDays(7)
            dateBrackets.add(Pair(nextDate, processedDate))
            processedDate = nextDate
        }

        return dateBrackets.map {
            bracket ->
            completedDates.count {
                with(it.resolvedDate) {
                    (isAfter(bracket.first) &&
                    isBefore(bracket.second)) ||
                    isEqual(bracket.second)
                }
            }
        }
    }
}

