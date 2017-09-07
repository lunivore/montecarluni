package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.Record
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordCreatorTest {

    private lateinit var events : Events
    private lateinit var parser : RecordCreator

    @Before
    fun createParser() {
        events = Events()
        parser = RecordCreator(events, {strings -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")})
    }

    @Test
    fun shouldFindTheDetailsOfWhenStoriesWereResolved() {

        // Given a CSV parser to which we're subscribed
        var results = listOf<Record>()
        events.recordsParsedNotification.subscribe { results = it}

        // When we pass it an input stream with some dates under "Resolved"
        val input = """Name,Created,Updated,Resolved,By
                story,ignored date,,2017-01-21 14:34:00,whoever
                story,ignored date,,2017-02-02 09:00:00,whoever
                story,ignored date,,2016-12-29 12:20:00,whoever
                """.trimIndent()
        val inputStream = ByteArrayInputStream(input.toByteArray())

        events.inputLoadedNotification.push(inputStream)

        // Then it should parse out those dates
        val expectedResults = listOf<LocalDateTime>(
                LocalDateTime.of(2017, 1, 21, 14, 34),
                LocalDateTime.of(2017, 2, 2, 9, 0),
                LocalDateTime.of(2016, 12, 29, 12, 20)).map { Record(it, null) }

        assertEquals(expectedResults, results)
    }

    @Test
    fun shouldIncludeLastUpdatedDatesInCaseResolvedDatesEmpty() {
        // Given a CSV parser to which we're subscribed
        var results = listOf<Record>()
        events.recordsParsedNotification.subscribe { results = it }

        // Given a CSV with some resolved dates empty but updated dates existing
        val input = """Name,Created,Updated,Resolved,By
                story,ignored date,2017-05-21 10:01:00,, whoever
                story,ignored date,2017-05-02 11:00:00,2017-05-22 10:00:00,whoever
                story,ignored date,2017-05-12 14:00:00,,whoever
                story,ignored date,2017-01-12 11:00:00,,whoever
                """.trimIndent()

        val inputStream = ByteArrayInputStream(input.toByteArray())

        // When we parse that input
        events.inputLoadedNotification.push(inputStream)

        // Then the records should include the updated dates as well as the created ones
        val expectedResults = listOf<Record>(
                Record(null, LocalDateTime.of(2017,5,21,10,1)),
                Record(LocalDateTime.of(2017, 5, 22, 10, 0), LocalDateTime.of(2017, 5, 2, 11, 0)),
                Record(null, LocalDateTime.of(2017, 5, 12, 14, 0)),
                Record(null, LocalDateTime.of(2017, 1, 12, 11, 0))
        )

        assertEquals(expectedResults, results)

    }

    fun createCsvInputStreamWithResolvedDate(datetime: String): InputStream {
        val input = """Name,Created,Updated,Resolved,By
                    story,ignored date,,$datetime,whoever
                    """.trimIndent()
        return ByteArrayInputStream(input.toByteArray())
    }

}
