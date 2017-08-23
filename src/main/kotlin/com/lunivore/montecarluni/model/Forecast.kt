package com.lunivore.montecarluni.model

import java.time.LocalDate

data class Forecast(val dataPoints : List<DataPoint>) {

}

data class DataPoint(val probability : Int, val forecastDate : LocalDate)
