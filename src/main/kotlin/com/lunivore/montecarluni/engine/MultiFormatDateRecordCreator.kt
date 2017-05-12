package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.Record
import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MultiFormatDateRecordCreator : ICreateRecordsFromCvs {

    private val alreadyCheckedExceptionMsg = "This should never happen but checking it keeps Kotlin happy."

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
    override fun parseResolvedDates(stream: InputStream): List<Record> {
        val csv = CSVReader(InputStreamReader(stream))
        var lines = csv.readAll()
        if (lines.size < 2) { throw IllegalArgumentException("Csv file requires a header row and at least one row of data.") }

        val resolvedIndex = lines[0].indexOfFirst { it == "Resolved" }
        val updatedIndex = lines[0].indexOfFirst { it == "Updated" }

        val resolveDatesAsLines = extractDate(lines, resolvedIndex)
        val updatedDatesAsLines = extractDate(lines, updatedIndex)

        val candidatesForDateFormat = resolveDatesAsLines.plus(updatedDatesAsLines).filter { it.isNotEmpty() }
        val dateFormat = parseDateFormat(candidatesForDateFormat)

        return resolveDatesAsLines.zip(updatedDatesAsLines).map {
            Record(if (it.first.isEmpty()) null else LocalDateTime.parse(it.first, dateFormat),
                    if (it.second.isEmpty()) null else LocalDateTime.parse(it.second, dateFormat)) }

    }

    private fun extractDate(lines: MutableList<Array<String>>, resolvedIndex: Int): List<String> {
        return lines.subList(1, lines.size)
                .fold(listOf<String>()) { dates, value ->
                    if (value != null) dates.plus(value[resolvedIndex])
                    else dates
                }
    }

    private fun  parseDateFormat(dateTimes: List<String>): DateTimeFormatter? {
        val pattern = Regex("\\d+.(?:\\w|\\d)+.\\d+ \\d*:\\d*\\s?\\w*")

        if (!pattern.matches(dateTimes[0])) {
            throw IllegalArgumentException("The first date found in the Resolved column - ${dateTimes[0]} - did not match any recognized pattern.")
        }

        val yearOrDayFormat = parseYearOrDayFormat(dateTimes)
        val monthFormat = parseMonthFormat(dateTimes)

        var timeFormat = parseTimeFormat(dateTimes)
        val delimiter = parseDelimiter(dateTimes)

        var dateFormat = ""
        dateFormat += yearOrDayFormat.first
        dateFormat += delimiter
        dateFormat += monthFormat
        dateFormat += delimiter
        dateFormat += yearOrDayFormat.second
        dateFormat += " "
        dateFormat += timeFormat

        return DateTimeFormatter.ofPattern(dateFormat)
    }

    private fun  parseMonthFormat(dateTimes: List<String>): String {
        val pattern = Regex("\\d+\\D((?:\\w|\\d)+)\\D\\d+ .*")
        val result = pattern.matchEntire(dateTimes[0]) ?: throw Exception(alreadyCheckedExceptionMsg)

        var month = result.groups[1]?.value ?: ""
        var monthIsDigits = Regex("\\d+").matches(month)

        var singleDigitMonth = if (!monthIsDigits) false else {
            dateTimes.any { Regex("\\d+\\D\\d\\D\\d+ .*").matches(it) }
        }

        var longMonth = if (monthIsDigits) false else {
            dateTimes.any { Regex("\\d+\\D\\w\\w\\w(?:\\w)+\\D\\d+ .*").matches(it) }
        }

        return if (monthIsDigits) { if (singleDigitMonth) "M" else "MM" }
            else if (longMonth) "MMMM" else "MMM"
    }

    private fun  parseDelimiter(dateTimes: List<String>): String {
        val pattern = Regex("\\d+(.)(?:\\w|\\d)+.\\d+.*")
        val result = pattern.matchEntire(dateTimes[0]) ?: throw Exception(alreadyCheckedExceptionMsg)

        return result.groups[1]?.value ?: ""
    }


    private fun  parseTimeFormat(dateTimes: List<String>): String {
        val pattern = Regex("\\d+.(?:\\w|\\d)+.\\d+ \\d*:\\d*\\s?(\\w*)")
        val result = pattern.matchEntire(dateTimes[0]) ?: throw Exception(alreadyCheckedExceptionMsg)

        var amPm = result.groups[1]?.value ?: ""
        val twentyfourHours = amPm.isEmpty()

        val doubleDigitHours = if (twentyfourHours) true else { dateTimes.all {Regex(".* \\d\\d:\\d\\d\\s?\\w*").matches(it)}}
        return if (twentyfourHours) "HH:mm" else { if (doubleDigitHours) "hh:mm a" else "h:mm a" }
    }

    private fun  parseYearOrDayFormat(dateTimes: List<String>): Pair<String, String>{

        val pattern = Regex("(\\d+).(?:\\w|\\d)+.(\\d+) \\d*:\\d*\\s?\\w*")
        val result = pattern.matchEntire(dateTimes[0]) ?: throw Exception(alreadyCheckedExceptionMsg)

        var yearOrDayInGroup1 = result.groups[1]?.value ?: ""
        var yearOrDayInGroup2 = result.groups[2]?.value ?: ""

        var yearFirst = yearOrDayInGroup1.length == 4
        var fourDigitYear = false
        if (!yearFirst) fourDigitYear = Regex("\\d\\d\\d\\d").matches(yearOrDayInGroup2)

        var doubleDigitDay = if (yearFirst == false) { dateTimes.all { Regex("(\\d\\d).*").matches(it) }} else false

        var firstFormat = if (yearFirst) "yyyy" else { if (doubleDigitDay) "dd" else "d" }
        var secondFormat = if (yearFirst) "dd" else { if (fourDigitYear) "yyyy" else "yy"}

        return Pair(firstFormat, secondFormat)
    }
}

