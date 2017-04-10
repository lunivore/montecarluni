package com.lunivore.montecarluni

import java.time.LocalDateTime

class DistributionCalculator {
    fun calculateDistribution(completedDates : List<LocalDateTime>): List<Int> {
        val lastCompletedDate = completedDates.max()
        val earliestCompletedDate = completedDates.min()

        val dateBrackets = mutableListOf<Pair<LocalDateTime, LocalDateTime>>()
        var processedDate = lastCompletedDate

        while (processedDate != null &&
                processedDate.compareTo(earliestCompletedDate) > 0) {

            var nextDate = processedDate.minusDays(7)
            dateBrackets.add(Pair(nextDate, processedDate))
            processedDate = nextDate
        }

        return dateBrackets.map {
            bracket ->
            completedDates.count {
                (it.isAfter(bracket.first) &&
                        it.isBefore(bracket.second)) ||
                        it.isEqual(bracket.second)
            }
        }
    }
}