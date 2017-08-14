package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.UserNotification
import com.lunivore.montecarluni.model.Record
import com.lunivore.montecarluni.model.WeeklyDistribution
import java.time.LocalDateTime
import org.apache.logging.log4j.LogManager

class DistributionCalculator(val events: Events) {

    companion object {
        val logger = LogManager.getLogger()!!
    }

    init {
        events.recordsParsedNotification.subscribe {
            if (!it.any { it.getResolvedOrLastUpdatedDate() == null }) {
                events.weeklyDistributionChangeNotification.push(WeeklyDistribution(calculateDistribution(it)))
            } else {
                logger.debug("Distribution had a problem:\n{}", it.map { "res: " + it.resolvedDate + " upd: " + it.lastUpdatedDate })
                events.messageNotification.push(UserNotification("Some of the records had neither a resolved nor an updated date"))
            }
        }
    }

    val neverUsed = LocalDateTime.of(1970, 1, 1, 0, 0)

    private fun calculateDistribution(completedDates: List<Record>): List<Int> {

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
                processedDate.compareTo(dateRange.first) >= 0) {

            var nextDate = processedDate.minusDays(7)
            dateBrackets.add(Pair(nextDate, processedDate))
            processedDate = nextDate
        }
        dateBrackets.reverse()
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


}

