package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.model.DataPoint
import java.time.format.DateTimeFormatter

class DataPointPresenter {
    val model : DataPoint

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    constructor(model: DataPoint) {
        this.model = model
    }

    val probabilityAsPercentageString : String
        get() {
            return "${model.probability}%"
        }

    val dateAsString : String
        get() {
            return formatter.format(model.forecastDate)
        }

}

