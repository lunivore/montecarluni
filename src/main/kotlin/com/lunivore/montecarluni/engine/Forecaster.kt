package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.DataPoint
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.ForecastRequest
import com.lunivore.montecarluni.model.WeeklyDistribution
import org.apache.logging.log4j.LogManager
import java.time.LocalDate
import java.util.*

class Forecaster(events: Events) {

    private val logger = LogManager.getLogger()
    private lateinit var  distribution: WeeklyDistribution
    private val random = Random(42)
    private val NUM_OF_CYCLES = 1000

    init {
        events.weeklyDistributionChangeNotification.subscribe {
            distribution = it
            if (distribution == WeeklyDistribution.EMPTY) {
                events.forecastNotification.push(Forecast.EMPTY)
            }
        }
        events.forecastRequest.subscribe {
            if (distribution != WeeklyDistribution.EMPTY){
                events.forecastNotification.push(generateForecast(it))
            }
        }
    }


    private fun  generateForecast(request: ForecastRequest): Forecast {
        logger.debug("Generating forecast; selection = ${request.useSelection}, selectedIndices = ${request.selectedIndices.size}")
        val allJourneys = IntArray(NUM_OF_CYCLES).map { getEndDate(request) }.sorted()
        val brackets = IntArray(21, {it * 5})
        brackets.sortDescending()

        return Forecast(brackets.map {
            DataPoint(it, if (it == 0) {allJourneys[0].minusDays(1)} else {allJourneys[it*(NUM_OF_CYCLES/100)-1]})
        })
    }

    private fun  getEndDate(request: ForecastRequest): LocalDate {
        val selection = request.selectedIndices
        val storiesToUse = if (request.useSelection) {
            distribution.storiesClosed.filterIndexed {
              index, _ ->  selection.contains(index)
            }} else {distribution.storiesClosed}

        var date = request.startDate ?: storiesToUse.last().range.end

        var done = 0
        while (done < request.numStories) {
            done = done + storiesToUse[random.nextInt(storiesToUse.size)].count
            date = date.plusDays(7)
        }
        return date
    }
}