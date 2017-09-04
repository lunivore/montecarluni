package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.model.StoriesClosedInWeek
import java.time.format.DateTimeFormatter

class StoriesClosedInWeekPresenter {
    private val model: StoriesClosedInWeek
    val rangeAsStrings: Pair<String, String>

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    constructor(model: StoriesClosedInWeek) {
        this.model = model
        this.rangeAsStrings = model.formatRange(formatter)
    }

    val rangeAsString : String
        get() {
            return "from ${rangeAsStrings.first} to ${rangeAsStrings.second}"
        }

    val count : String
        get() {
            return model.count.toString()
        }
}