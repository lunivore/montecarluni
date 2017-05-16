package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.Record
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class DistributionCalculatorTest {

    @Test
    fun shouldCountNumberOfTicketsResolvedInDifferentWeeks() {
        // Given a set of records weeks apart
        val records = listOf(1, 1, 1, 8, 15, 15, 22)
                .map { Record(LocalDateTime.of(2017, 1, it, 12, 0, 0), null) }

        // When we run them through the distribution calculator
        val results = DistributionCalculator().calculateDistribution(records)

        // Then it should provide the distribution
        val expectedDistribution = listOf(3, 1, 2, 1)

        assertEquals(expectedDistribution, results)
    }

    @Test
    fun shouldUseLastUpdatedDateIfResolvedDateBlank() {
        val hasResolvedDates = listOf(1, 3, 3, 8, 10, 15, 17, 22)
                .map { Record(LocalDateTime.of(2017, 1, it, 12, 0, 0), null) }
        val hasUpdatedDates = listOf(3, 8, 22)
                .map { Record(null, LocalDateTime.of(2017, 1, it, 12, 0, 0)) }
        val records = hasResolvedDates.plus(hasUpdatedDates)

        // When we run them through the distribution calculator
        val results = DistributionCalculator().calculateDistribution(records)

        // Then it should provide the distribution
        val expectedDistribution = listOf(1, 5, 2, 3)

        assertEquals(expectedDistribution, results)
    }

    @Test
    fun shouldComplainIfAnyRecordsHaveNoResolvedOrUpdatedDate() {
        val records = listOf(Record(null, null))

        try {
            DistributionCalculator().calculateDistribution(records)
            fail("Should have thrown an exception")
        } catch (e : IllegalArgumentException) {
            // Expected
        }
    }
}