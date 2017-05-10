package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.Record
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.LocalDateTime

class MultiFormatDateResolvedParserTest {

    @Test
    fun shouldFindTheDetailsOfWhenStoriesWereResolved() {
        // Given a CSV parser
        val parser = MultiFormatDateResolvedParser()

        // When we pass it an input stream with some dates under "Resolved"
        val input = """Name,Created,Resolved,By
                story,ignored date,21/Jan/17 2:34 PM,whoever
                story,ignored date,02/Feb/17 9:00 AM,whoever
                story,ignored date,29/Dec/16 12:20 PM,whoever
                """.trimIndent()
        val inputStream = ByteArrayInputStream(input.toByteArray())
        val results = parser.parseResolvedDates(inputStream)

        // Then it should parse out those dates
        val expectedResults = listOf<LocalDateTime>(
                LocalDateTime.of(2017, 1, 21, 14, 34),
                LocalDateTime.of(2017, 2, 2, 9, 0),
                LocalDateTime.of(2016, 12, 29, 12, 20)).map { Record(it) }

        assertEquals(expectedResults, results)
    }


    @Test
    fun shouldHandleMultipleDifferentDateTimeFormats() {
        // Given a CSV parser
        val parser = MultiFormatDateResolvedParser()

        // When we pass it input streams with very different date formats
        val inputStreams = listOf<InputStream>(
                createCsvInputStream("2017-04-03 08:25"),
                createCsvInputStream("01 Jan 2017 01:27 PM"),
                createCsvInputStream("8/9/2016 8:15 AM")                    )


        val results = inputStreams.map {parser.parseResolvedDates(it)}

        // Then it should be able to resolve those dates.
        val expectedResults = listOf(
                LocalDateTime.of(2017,4,3,8,25),
                LocalDateTime.of(2017,1,1,13,27),
                LocalDateTime.of(2016,9,8,8,15)).map { Record(it) }

        assertEquals(expectedResults, results.flatMap { it })
    }

    @Test
    fun shouldHandleDateFormatsWhereTheFormatIsNotObviousFromFirstDate() {
        // Given a CSV parser
        val parser = MultiFormatDateResolvedParser()

        // When we pass it an input stream with ambiguous formats
        // - completely ambiguous
        // - uses 2-day day
        // - uses single-digit hour
        // - uses long-format month
        val input = """Name,Created,Resolved,By
                story,ignored date,21/May/17 10:01 AM,whoever
                story,ignored date,02/May/17 11:00 AM,whoever
                story,ignored date,12/May/17 2:00 PM,whoever
                story,ignored date,12/January/17 11:00 AM,whoever
                """.trimIndent()

        val inputStream = ByteArrayInputStream(input.toByteArray())
        val results = parser.parseResolvedDates(inputStream)

        // Then it should still work them out from the rest of the dates
        val expectedResults = listOf<LocalDateTime>(
                LocalDateTime.of(2017, 5, 21, 10, 1),
                LocalDateTime.of(2017, 5, 2, 11, 0),
                LocalDateTime.of(2017, 5, 12, 14, 0),
                LocalDateTime.of(2017, 1, 12, 11, 0)).map{ Record(it) }

        assertEquals(expectedResults, results)
    }

    fun createCsvInputStream(datetime: String): InputStream {
        val input = """Name,Created,Resolved,By
                    story,ignored date,$datetime,whoever
                    """.trimIndent()
        return ByteArrayInputStream(input.toByteArray())
    }

}
