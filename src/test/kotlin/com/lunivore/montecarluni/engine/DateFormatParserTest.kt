package com.lunivore.montecarluni.engine

import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

class DateFormatParserTest {

    @Test
    fun `should parse JIRA's expected date format`() {

        // Given a list of dates in Jira's godawful format
        val input = listOf("21/Jan/17 2:34 PM", "02/Feb/17 9:00 AM", "29/Dec/16 12:20 PM")

        // When we parse the format of the dates
        val formatter = DateFormatParser()(input)
        val results = input.map { LocalDateTime.parse(it, formatter)}

        val expectedResults = listOf<LocalDateTime?>(
                LocalDateTime.of(2017, 1, 21, 14, 34),
                LocalDateTime.of(2017, 2, 2, 9, 0),
                LocalDateTime.of(2016, 12, 29, 12, 20))

        Assert.assertEquals(expectedResults, results)
    }


    @Test
    fun `should handle multiple different date formats`() {
        // Given a number of different types of date format
        val input = listOf(listOf("2017-04-03 08:25"), listOf("01 Jan 2017 01:27 PM"), listOf("8/9/2016 8:15 AM"))

        // When we parse the format of the dates
        val formatters = input.map {DateFormatParser()(it)}
        val results = input.zip(formatters, {i, f -> i.map {LocalDateTime.parse(it, f)}})


        // Then it should be able to resolve those dates.
        val expectedResults = listOf(
                LocalDateTime.of(2017,4,3,8,25),
                LocalDateTime.of(2017,1,1,13,27),
                LocalDateTime.of(2016,9,8,8,15))

        Assert.assertEquals(expectedResults, results.flatMap { it })
    }

    @Test
    fun `should handle date formats where the format is not obvious`() {
        // Given a list of date strings with ambiguous formats
        // - completely ambiguous
        // - uses 2-day day
        // - uses single-digit hour
        // - uses long-format month
        val input = listOf("21/May/17 10:01 AM", "02/May/17 11:00 AM", "12/May/17 2:00 PM", "12/January/17 11:00 AM")

        // When we parse the format of the dates
        val formatter = DateFormatParser()(input)
        val results = input.map { LocalDateTime.parse(it, formatter)}

        // Then it should still work them out from the rest of the dates
        val expectedResults = listOf<LocalDateTime>(
                LocalDateTime.of(2017, 5, 21, 10, 1),
                LocalDateTime.of(2017, 5, 2, 11, 0),
                LocalDateTime.of(2017, 5, 12, 14, 0),
                LocalDateTime.of(2017, 1, 12, 11, 0))

        Assert.assertEquals(expectedResults, results)
    }

    @Test
    fun `should handle dates without times`() {
        // Given a list of dates without times
        val input = listOf("21/Jan/17", "02/Feb/17", "29/Dec/16")

        // When we parse the format of the dates
        val formatter = DateFormatParser()(input)
        val results = input.map { LocalDateTime.parse(it, formatter)}

        // Then they should have no time on them
        val expectedResults = listOf<LocalDateTime?>(
                LocalDateTime.of(2017, 1, 21,0, 0, 0),
                LocalDateTime.of(2017, 2, 2, 0, 0, 0),
                LocalDateTime.of(2016, 12, 29, 0, 0, 0))

        Assert.assertEquals(expectedResults, results)
    }

}