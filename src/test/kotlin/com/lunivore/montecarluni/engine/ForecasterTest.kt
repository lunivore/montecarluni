package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.DateRange
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.StoriesClosedInWeek
import com.lunivore.montecarluni.model.WeeklyDistribution
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ForecasterTest {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @Test
    fun `should be able to provide a simple probability distribution from a weekly sample`() {
        // Given a completely even sample set that finishes on 25th Feb
        val startDate = LocalDate.of(2017, 1, 1)
        val distribution = WeeklyDistribution(toDistribution(listOf(4, 4, 4, 4, 4, 4, 4, 4), startDate))

        // And a forecaster listening for events, and to which we are listening
        val events = Events()
        val forecaster = Forecaster(events)

        var result : Forecast = Forecast(listOf())
        events.forecastNotification.subscribe { result = it }

        // When we request a forecast for that distribution with a number of stories
        // that we should finish in exactly 8 weeks
        events.weeklyDistributionChangeNotification.push(distribution)
        events.forecastRequest.push(32)

        // Then we should be provided with a really predictable result
        val expectedForecast =
            """
            100% | 2017-04-23
            95% | 2017-04-23
            90% | 2017-04-23
            85% | 2017-04-23
            80% | 2017-04-23
            75% | 2017-04-23
            70% | 2017-04-23
            65% | 2017-04-23
            60% | 2017-04-23
            55% | 2017-04-23
            50% | 2017-04-23
            45% | 2017-04-23
            40% | 2017-04-23
            35% | 2017-04-23
            30% | 2017-04-23
            25% | 2017-04-23
            20% | 2017-04-23
            15% | 2017-04-23
            10% | 2017-04-23
            5% | 2017-04-23
            0% | 2017-04-22
            """
        assertEquals(expectedForecast.trimIndent(),
                result.dataPoints.map { "${it.probability}% | ${it.forecastDate.format(formatter)}" }
                        .joinToString(separator="\n"))
    }

    private fun toDistribution(numOfStoriesClosed: List<Int>, startDate: LocalDate): List<StoriesClosedInWeek> {
        return numOfStoriesClosed.mapIndexed { index, distribution ->
            StoriesClosedInWeek(DateRange(startDate.plusDays(7 * index.toLong()), startDate.plusDays((7 * index.toLong() + 7))), distribution)
        }
    }
}