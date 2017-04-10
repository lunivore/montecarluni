package com.lunivore.montecarluni

import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CsvParser {

    private val dateFormat = DateTimeFormatter.ofPattern("dd/MMM/yy h:mm a") // 05/Apr/17 2:35 PM

    fun parseCompletedDates(stream: InputStream?): List<LocalDateTime> {
        val csv = CSVReader(InputStreamReader(stream))
        var lines = csv.readAll()

        val doneIndex = lines[0].indexOfFirst { it == "Resolved" }
        return lines.subList(1, lines.size)
                .fold(listOf<LocalDateTime>()) { dates, value ->
                    if (value != null) dates.plus(LocalDateTime.parse(value[doneIndex], dateFormat))
                    else dates
                }
    }
}