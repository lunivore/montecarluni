package com.lunivore.montecarluni

import com.opencsv.CSVReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DistributionCalculator {

    private var  filename: String = ""
    private var completedDates = listOf<LocalDateTime>()
    private val dateFormat = DateTimeFormatter.ofPattern("dd/MMM/yy h:mm a") // 05/Apr/17 2:35 PM

    private var distributionChangedHandler : (List<Int>) -> Unit = {}

    fun getDistribution() : List<Int> {
        var stream = this.javaClass.getResourceAsStream(filename)

        if (stream == null) {
            stream = FileInputStream(filename)
        }

        if (stream == null) {
            throw Exception("Could not load file at $filename")
        }

        val csv = CSVReader(InputStreamReader(stream))
        var lines = csv.readAll()

        val doneIndex = lines[0].indexOfFirst{ it == "Resolved" }
        completedDates = lines.subList(1, lines.size)
                .fold(listOf<LocalDateTime>()) { dates, value ->
                    if (value != null) dates.plus(LocalDateTime.parse(value[doneIndex], dateFormat))
                    else dates }

        val lastCompletedDate = completedDates.max()
        val earliestCompletedDate = completedDates.min()

        val dateBrackets = mutableListOf<Pair<LocalDateTime, LocalDateTime>>()
        var processedDate = lastCompletedDate

        while (processedDate != null &&
                processedDate.compareTo(earliestCompletedDate) > 0) {

            var nextDate = processedDate.minusDays(7)
            dateBrackets.add(Pair(nextDate, processedDate))
            processedDate = nextDate
        }

        return dateBrackets.map {
            bracket -> completedDates.count {
            (it.isAfter(bracket.first) &&
                    it.isBefore(bracket.second)) ||
                    it.isEqual(bracket.second)}
        }
    }

    fun requestImport() {
        val results = getDistribution()

        distributionChangedHandler(results)
    }

    fun  filenameChanged(filename: String?) {
        this.filename = filename ?: "";
    }

    fun setDistributionHandler(handler : (List<Int>) -> Unit) {
        distributionChangedHandler = handler
    }
}