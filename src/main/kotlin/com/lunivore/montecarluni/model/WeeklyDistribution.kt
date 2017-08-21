package com.lunivore.montecarluni.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class WeeklyDistribution(val storiesClosed : List<StoriesClosedInWeek>) {
    val lineSeparatorForExcel = "\n"

    val distributionAsString: String
        get(){
            return storiesClosed.map { it.count }.joinToString(separator = lineSeparatorForExcel)
        }
}

data class StoriesClosedInWeek(val range : DateRange, val count : Int)

data class DateRange(val start : LocalDate, val end : LocalDate) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val asString: String
        get() {
            return "from ${start.format(formatter)} to ${end.format(formatter)}"
        }
}
