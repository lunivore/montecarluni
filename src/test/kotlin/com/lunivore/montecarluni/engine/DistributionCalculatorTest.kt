package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.Record
import com.lunivore.montecarluni.model.UserNotification
import com.lunivore.montecarluni.model.WeeklyDistribution
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class DistributionCalculatorTest {

    private lateinit var events : Events
    private lateinit var calculator: DistributionCalculator

    @Before
    fun createParser() {
        events = Events()
        calculator = DistributionCalculator(events)
    }

    @Test
    fun shouldCountNumberOfTicketsResolvedInDifferentWeeks() {
        // Given a DistributionCalculator to which we're subscribed
        var results = WeeklyDistribution(listOf<Int>())
        events.weeklyDistributionChangeNotification.subscribe { results = it }

        // Given a set of records weeks apart
        val records = listOf(1, 1, 1, 8, 15, 15, 22)
                .map { Record(LocalDateTime.of(2017, 1, it, 12, 0, 0), null) }

        // When we run them through the distribution calculator
        events.recordsParsedNotification.push(records)

        // Then it should provide the distribution
        val expectedDistribution = WeeklyDistribution(listOf(3, 1, 2, 1))

        assertEquals(expectedDistribution, results)
    }

    @Test
    fun shouldUseLastUpdatedDateIfResolvedDateBlank() {
        // Given a DistributionCalculator to which we're subscribed
        var results = WeeklyDistribution(listOf<Int>())
        events.weeklyDistributionChangeNotification.subscribe { results = it }

        val hasResolvedDates = listOf(1, 3, 3, 8, 10, 15, 17, 22)
                .map { Record(LocalDateTime.of(2017, 1, it, 12, 0, 0), null) }
        val hasUpdatedDates = listOf(3, 8, 22)
                .map { Record(null, LocalDateTime.of(2017, 1, it, 12, 0, 0)) }
        val records = hasResolvedDates.plus(hasUpdatedDates)

        // When we run them through the distribution calculator
        events.recordsParsedNotification.push(records)
        // Then it should provide the distribution
        val expectedDistribution = WeeklyDistribution(listOf(1, 5, 2, 3))

        assertEquals(expectedDistribution, results)
    }

    @Test
    fun shouldComplainIfAnyRecordsHaveNoResolvedOrUpdatedDate() {

        // Given a DistributionCalculator to which we're subscribed for errors
        var problem : UserNotification? = null
        events.messageNotification.subscribe {problem = it}

        // And a record with neither updated nor resolved date
        val records = listOf(Record(null, null))

        // When we run it through the distribution calculator
        events.recordsParsedNotification.push(records)

        // Then we should have received notification of a problem
        val expectedProblem = UserNotification("Some of the records had neither a resolved nor an updated date")
        assertEquals(expectedProblem, problem)
    }
}