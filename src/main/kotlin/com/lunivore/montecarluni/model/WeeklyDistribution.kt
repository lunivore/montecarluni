package com.lunivore.montecarluni.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class WeeklyDistribution(val storiesClosed : List<StoriesClosedInWeek>) {
    val lineSeparatorForExcel = "\n"

    val distributionAsString: String
        get(){
            return storiesClosed.map { it.count }.joinToString(separator = lineSeparatorForExcel)
        }

    fun distributionAsString(selection : List<Int>) : String {
        return storiesClosed.filterIndexed{index, content -> selection.contains(index)}
                .map { it.count }.joinToString(separator = lineSeparatorForExcel)
    }
}

data class StoriesClosedInWeek(val range : DateRange, val count : Int) {
    fun  formatRange(formatter: DateTimeFormatter): Pair<String, String> {
        return range.format(formatter)
    }
}

data class DateRange(val start : LocalDate, val end : LocalDate) {
    fun  format(formatter: DateTimeFormatter): Pair<String, String> { return Pair(formatter.format(start), formatter.format(end))}
}
