package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.DataPoint
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.WeeklyDistribution
import java.time.LocalDate
import java.util.*

class Forecaster(events: Events) {

    private lateinit var  distribution: WeeklyDistribution
    private val random = Random(42)

    init {
        events.weeklyDistributionChangeNotification.subscribe { distribution = it }
        events.forecastRequest.subscribe {
            events.forecastNotification.push(generateForecast(it))
        }
    }

    private fun  generateForecast(numStories: Int): Forecast {
        val allJourneys = IntArray(500).map { getEndDate(numStories) }.sorted()
        val brackets = IntArray(21, {it * 5})
        brackets.sortDescending()

        return Forecast(brackets.map {
            DataPoint(it, if (it == 0) {allJourneys[0].minusDays(1)} else {allJourneys[it*5-1]})
        })
    }

    private fun  getEndDate(numStories: Int): LocalDate {
        var date = distribution.storiesClosed.last().range.end

        var done = 0
        while (done < numStories) {
            done = done + distribution.storiesClosed[random.nextInt(distribution.storiesClosed.size)].count
            date = date.plusDays(7)
        }
        return date
    }
}