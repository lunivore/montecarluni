package com.lunivore.montecarluni.model

data class WeeklyDistribution(val countsByWeek: List<Int>) {
    val lineSeparatorForExcel = "\n"

    fun  asText(): String {
        return countsByWeek.joinToString(separator = lineSeparatorForExcel)
    }
}