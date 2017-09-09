package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.DateRange
import com.lunivore.montecarluni.model.Record
import com.lunivore.montecarluni.model.WorkItemsClosedInWeek
import org.apache.logging.log4j.LogManager
import java.time.LocalDateTime

class WeeklyDistributionCalculator() {

    companion object {
        val logger = LogManager.getLogger()!!
    }


    val neverUsed = LocalDateTime.of(1970, 1, 1, 0, 0)

    fun calculateDistribution(completedDates: List<Record>): List<WorkItemsClosedInWeek> {

        val dateRange = findFirstAndLastCompletedDates(completedDates)
        val dateBrackets = calculateDateBrackets(dateRange)

        return dateBrackets.map {
            bracket ->
            WorkItemsClosedInWeek(DateRange(bracket.first.toLocalDate(), bracket.second.toLocalDate().minusDays(1)),
            completedDates.count {
                val date = it.resolvedOrLastUpdatedDate
                date != null &&
                        with(date) {
                            (isAfter(bracket.first) &&
                                    isBefore(bracket.second)) ||
                                    isEqual(bracket.second)
                        }
            })
        }
    }

    private fun calculateDateBrackets(dateRange : Pair<LocalDateTime, LocalDateTime>):
            MutableList<Pair<LocalDateTime, LocalDateTime>> {
        val dateBrackets = mutableListOf<Pair<LocalDateTime, LocalDateTime>>()
        var processedDate = dateRange.second

        while (processedDate != null &&
                processedDate.compareTo(dateRange.first) >= 0) {

            var nextDate = processedDate.minusDays(7)
            dateBrackets.add(Pair(nextDate, processedDate))
            processedDate = nextDate
        }
        dateBrackets.reverse()
        return dateBrackets
    }

    private fun findFirstAndLastCompletedDates(completedDates: List<Record>): Pair<LocalDateTime, LocalDateTime> {

        val earliestCompletedDate = completedDates.minBy { it.resolvedOrLastUpdatedDate ?: neverUsed }
                ?.resolvedOrLastUpdatedDate?.toLocalDate()?.atStartOfDay()
        val lastCompletedDate = completedDates.maxBy { it.resolvedOrLastUpdatedDate ?: neverUsed }
                ?.resolvedOrLastUpdatedDate?.plusDays(1)?.toLocalDate()?.atStartOfDay()

        if (earliestCompletedDate == null || lastCompletedDate == null) {
            throw Exception("Could not find records to parse for date brackets. (Montecarluni should checked for this already!)")
        }

        return Pair(earliestCompletedDate, lastCompletedDate)
    }


}

