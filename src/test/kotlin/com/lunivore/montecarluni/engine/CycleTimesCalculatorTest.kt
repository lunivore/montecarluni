package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.CycleTime
import com.lunivore.montecarluni.model.Record
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class CycleTimesCalculatorTest {

    @Test
    fun `should provide a list of the gaps between work items`() {
        // Given a list of records
        val dates = listOf(LocalDateTime.of(2017, 1, 2, 12, 0, 0),
                LocalDateTime.of(2017, 1, 3, 16, 0, 0), // this one is out of order
                LocalDateTime.of(2017, 1, 3, 14, 0, 0),
                LocalDateTime.of(2017, 1, 7, 16, 0, 0))
        val records = dates
            .map { Record(it, null) }

        // When we ask for the cycle times from the records
        val result = CycleTimesCalculator().calculateCycleTimes(records)

        // Then it should give us back a list of the gaps
        val expectedResult = listOf(
                CycleTime("", dates[0], null),
                CycleTime("", dates[2], Duration.ofHours(26)),
                CycleTime("", dates[1], Duration.ofHours(2)),
                CycleTime("", dates[3], Duration.ofHours(96))
        )

        assertEquals(expectedResult, result)
    }
}
