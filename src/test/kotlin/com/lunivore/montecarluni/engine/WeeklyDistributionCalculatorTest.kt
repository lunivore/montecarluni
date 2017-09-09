package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.DateRange
import com.lunivore.montecarluni.model.Record
import com.lunivore.montecarluni.model.WorkItemsClosedInWeek
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class WeeklyDistributionCalculatorTest {

    @Test
    fun `should count number of work items resolved in different weeks`() {
        // Given a set of records weeks apart
        val records = listOf(1, 1, 1, 8, 15, 15, 22)
                .map { Record(LocalDateTime.of(2017, 1, it, 12, 0, 0), null) }

        // When we run them through the weeklyDistribution calculator
        val results = WeeklyDistributionCalculator().calculateDistribution(records)

        // Then it should provide the weeklyDistribution
        val dec31 = LocalDate.of(2016, 12, 31)
        val expectedDistribution = listOf(Pair(-5L, 1L), Pair(2L, 8L), Pair(9L, 15L), Pair(16L, 22L))
                .map { DateRange(dec31.plusDays(it.first), dec31.plusDays(it.second)) }
                .zip(listOf(3, 1, 2, 1), {d, i -> WorkItemsClosedInWeek(d, i)})

        assertEquals(expectedDistribution, results)
    }
}


