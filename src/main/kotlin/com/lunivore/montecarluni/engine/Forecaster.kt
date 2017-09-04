package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.DataPoint
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.ForecastRequest
import com.lunivore.montecarluni.model.WeeklyDistribution
import java.time.LocalDate
import java.util.*

class Forecaster(events: Events) {

    private lateinit var  distribution: WeeklyDistribution
    private val random = Random(42)
    private val NUM_OF_CYCLES = 1000
    private var  selectedIndices: List<Int>? = null

    init {
        events.weeklyDistributionChangeNotification.subscribe { distribution = it }
        events.weeklyDistributionSelectionRequest.subscribe { selectedIndices = it }
        events.forecastRequest.subscribe {
            events.forecastNotification.push(generateForecast(it))
        }
    }


    private fun  generateForecast(request: ForecastRequest): Forecast {
        val allJourneys = IntArray(NUM_OF_CYCLES).map { getEndDate(request) }.sorted()
        val brackets = IntArray(21, {it * 5})
        brackets.sortDescending()

        return Forecast(brackets.map {
            DataPoint(it, if (it == 0) {allJourneys[0].minusDays(1)} else {allJourneys[it*(NUM_OF_CYCLES/100)-1]})
        })
    }

    private fun  getEndDate(request: ForecastRequest): LocalDate {
        val selection = selectedIndices
        val storiesToUse = if (selection == null) { distribution.storiesClosed }
            else {distribution.storiesClosed.filterIndexed {
              index, journey ->  selection.contains(index)
            }}

        var date = request.startDate ?: storiesToUse.last().range.end

        var done = 0
        while (done < request.numStories) {
            done = done + storiesToUse[random.nextInt(storiesToUse.size)].count
            date = date.plusDays(7)
        }
        return date
    }
}