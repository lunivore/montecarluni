package com.lunivore.montecarluni.engine

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class DateFormatParser : (List<String>) -> DateTimeFormatter {

    private val alreadyCheckedExceptionMsg = "This should never happen but checking it keeps Kotlin happy."

    override fun invoke(p1: List<String>): DateTimeFormatter {
        return parseDateFormat(p1)
    }
    private fun  parseDateFormat(dateTimes: List<String>): DateTimeFormatter {
        val pattern = Regex("\\d+.(?:\\w|\\d)+.\\d+( \\d*:\\d*\\s?\\w*)?")

        if (!pattern.matches(dateTimes[0])) {
            throw IllegalArgumentException("The first date found - ${dateTimes[0]} - did not match any recognized pattern.")
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
        dateFormat += timeFormat

        if (timeFormat.isEmpty()) {
            dateFormat += "[ HH:mm:ss]"
            return DateTimeFormatterBuilder().appendPattern(dateFormat)
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
        } else { return DateTimeFormatter.ofPattern(dateFormat) }
    }

    private fun  parseMonthFormat(dateTimes: List<String>): String {
        val pattern = Regex("\\d+\\D((?:\\w|\\d)+)\\D\\d+( )?.*")
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
        val pattern = Regex("\\d+.(?:\\w|\\d)+.\\d+( \\d*:\\d*\\s?(\\w*))?")
        val result = pattern.matchEntire(dateTimes[0]) ?: throw Exception(alreadyCheckedExceptionMsg)

        val doesNotHaveTime = result.groups[2] == null
        if (doesNotHaveTime) { return "" }

        var amPm = result.groups[2]?.value ?: ""
        val twentyfourHours = amPm.isEmpty()

        val doubleDigitHours = if (twentyfourHours) true else { dateTimes.all {Regex(".* \\d\\d:\\d\\d\\s?\\w*").matches(it)}}
        return " " + if (twentyfourHours) "HH:mm" else { if (doubleDigitHours) "hh:mm a" else "h:mm a" }
    }

    private fun  parseYearOrDayFormat(dateTimes: List<String>): Pair<String, String>{

        val pattern = Regex("(\\d+).(?:\\w|\\d)+.(\\d+)(?: \\d*:\\d*\\s?\\w*)?")
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