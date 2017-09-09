package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

class DistributionsMarshallTest {

    @Test
    fun `should assemble weekly and cycle time distributions`() {
        // Given functions that will provide weekly and cycle time distributions
        val weeklyDistribution = listOf(
                WorkItemsClosedInWeek(DateRange(LocalDate.of(2017, 4, 3),LocalDate.of(2017, 4, 10)), 10))
        val cycleTimes = listOf(CycleTime("", LocalDateTime.of(2017, 4, 3, 12, 0, 0), Duration.ofHours(72)))

        // And a distribution marshall using them, to which we're listening
        val events = Events()
        var result = Distributions.EMPTY
        events.distributionChangeNotification.subscribe { result = it }
        val marshall = DistributionsMarshall(events,
                {_ : Any -> weeklyDistribution},
                {_ : Any -> cycleTimes})

        // When we notify it of records being successfully parsed
        events.recordsParsedNotification.push(listOf(Record(LocalDateTime.of(2017, 4, 3, 12, 0), null)))

        // Then it should marshall the results from the delegated functions
        val expectedResult = Distributions(weeklyDistribution, cycleTimes)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should pass back empty distributions if records are empty`() {
        // Given a distribution marshall to which we're listening
        val events = Events()
        var result : Distributions? = null
        events.distributionChangeNotification.subscribe { result = it }
        val marshall = DistributionsMarshall(events,
                {_ : Any -> throw(AssertionError("Should not need to call this"))},
                {_ : Any -> throw(AssertionError("Should not need to call this"))})

        // When we notify it of empty records
        events.recordsParsedNotification.push(listOf())

        // Then it should tell us of empty distributions
        assertEquals(Distributions.EMPTY, result)
    }
}

