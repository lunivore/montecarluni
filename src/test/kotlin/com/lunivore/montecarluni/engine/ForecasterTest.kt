package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.*
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
        events.forecastRequest.push(ForecastRequest(32, null))

        // Then we should be provided with a really predictable result
        val expectedForecast =listOf(100, 95, 90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5, 0)
                .map {if (it == 0) "0% | 2017-04-22" else "$it% | 2017-04-23"}
                .joinToString(separator="\n")
        assertEquals(expectedForecast,
                result.dataPoints.map { "${it.probability}% | ${it.forecastDate.format(formatter)}" }
                        .joinToString(separator="\n"))
    }

    @Test
    fun `should be able to start forecast from a given date`() {
        // Given a completely even sample set that finishes on 25th Feb
        val startDate = LocalDate.of(2017, 1, 1)
        val distribution = WeeklyDistribution(toDistribution(listOf(4, 4, 4, 4, 4, 4, 4, 4), startDate))

        // And a forecaster listening for events, and to which we are listening
        val events = Events()
        val forecaster = Forecaster(events)

        var result : Forecast = Forecast(listOf())
        events.forecastNotification.subscribe { result = it }

        // When we request a forecast for that distribution with a number of stories
        // that we should finish in exactly 8 weeks, but we're adding another week
        // (on top of the 8 weeks of distribution data)
        events.weeklyDistributionChangeNotification.push(distribution)
        events.forecastRequest.push(ForecastRequest(32, LocalDate.of(2017, 3, 5)))

        // Then we should be provided with a really predictable result
        val expectedForecast =listOf(100, 95, 90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5, 0)
                .map {if (it == 0) "0% | 2017-04-29" else "$it% | 2017-04-30"}
                .joinToString(separator="\n")
        assertEquals(expectedForecast,
                result.dataPoints.map { "${it.probability}% | ${it.forecastDate.format(formatter)}" }
                        .joinToString(separator="\n"))
    }

    @Test
    fun `should be able to use a partial distribution`() {
        // Given a bimodal sample set that finishes on 25th Feb
        val startDate = LocalDate.of(2017, 1, 1).minusDays(7)
        val distribution = WeeklyDistribution(toDistribution(listOf(8, 8, 4, 4, 4, 4, 4, 4, 4), startDate))

        // And a forecaster listening for events, and to which we are listening
        val events = Events()
        val forecaster = Forecaster(events)

        var result : Forecast = Forecast(listOf())
        events.forecastNotification.subscribe { result = it }

        // When we request a forecast for the distribution from the point the team ramped up
        events.weeklyDistributionChangeNotification.push(distribution)
        events.weeklyDistributionSelectionRequest.push(listOf(2, 3, 4, 5, 6, 7, 8))
        events.forecastRequest.push(ForecastRequest(32, null))

        // Then we should be provided with exactly the same distribution as before.
        val expectedForecast =listOf(100, 95, 90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5, 0)
                .map {if (it == 0) "0% | 2017-04-22" else "$it% | 2017-04-23"}
                .joinToString(separator="\n")
        assertEquals(expectedForecast,
                result.dataPoints.map { "${it.probability}% | ${it.forecastDate.format(formatter)}" }
                        .joinToString(separator="\n"))

    }

    private fun toDistribution(numOfStoriesClosed: List<Int>, startDate: LocalDate): List<StoriesClosedInWeek> {
        return numOfStoriesClosed.mapIndexed { index, distribution ->
            StoriesClosedInWeek(DateRange(startDate.plusDays(7 * index.toLong()), startDate.plusDays((7 * index.toLong() + 7))), distribution)
        }
    }
}