package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.model.WorkItemsClosedInWeek
import java.time.format.DateTimeFormatter

class WorkItemsDoneInWeekPresenter {
    private val model: WorkItemsClosedInWeek
    val rangeAsStrings: Pair<String, String>

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    constructor(model: WorkItemsClosedInWeek) {
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