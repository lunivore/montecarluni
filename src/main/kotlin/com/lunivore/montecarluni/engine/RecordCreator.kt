package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.Record
import com.lunivore.montecarluni.model.UserNotification
import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This will parse any date in UK or standard format, providing that a date with the year first also has
 * 2-digit month and day (it is assumed that sensible people continue to be consistently sensible).
 *
 * Delimiters will also be parsed, so anything sensible is fine.
 *
 * Months are supported with both 3-letter and full word versions.
 *
 * Formats with the month at the start do not make sense and will not be supported.
 */
class RecordCreator(private val events: Events, private val dateFormatParser : (List<String>) -> DateTimeFormatter) {

    constructor(events : Events) : this(events, DateFormatParser()){

    }

    init {
        events.inputLoadedNotification.subscribe {
            events.recordsParsedNotification.push(parseResolvedDates(it))
        }
    }

    private fun parseResolvedDates(stream: InputStream): List<Record> {
        val csv = CSVReader(InputStreamReader(stream))
        val lines = csv.readAll()
        if (lines.size < 2) { throw IllegalArgumentException("Csv file requires a header row and at least one row of data.") }

        val resolvedIndex = lines[0].indexOfFirst { it == "Resolved" }
        val updatedIndex = lines[0].indexOfFirst { it == "Updated" }

        val resolveDatesAsLines = extractDate(lines, resolvedIndex)
        val updatedDatesAsLines = extractDate(lines, updatedIndex)

        val candidatesForDateFormat = resolveDatesAsLines.plus(updatedDatesAsLines).filter { it.isNotEmpty() }
        val dateFormat = dateFormatParser(candidatesForDateFormat)

        val result = resolveDatesAsLines.zip(updatedDatesAsLines)
        if (result.any {it.first.isEmpty() && it.second.isEmpty()}) {
            events.messageNotification.push(UserNotification("Could not find resolved or last updated dates for some records"))
            return listOf()
        } else {

            return result.map {
                Record(if (it.first.isEmpty()) null else LocalDateTime.parse(it.first, dateFormat),
                        if (it.second.isEmpty()) null else LocalDateTime.parse(it.second, dateFormat))
            }
        }
    }

    private fun extractDate(lines: MutableList<Array<String>>, resolvedIndex: Int): List<String> {
        return lines.subList(1, lines.size)
                .fold(listOf<String>()) { dates, value ->
                    if (!value.isEmpty()) dates.plus(value[resolvedIndex])
                    else dates
                }
    }


}

