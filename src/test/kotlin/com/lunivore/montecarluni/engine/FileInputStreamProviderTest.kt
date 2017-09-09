package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.UserNotification
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.io.InputStream


class FileInputStreamProviderTest() {

    @Test
    fun `should load a file and convert to an input stream`() {
        // Given a file that we know exists
        var license = "LICENSE.txt"

        // And an input stream provider that we're listening to
        val events = Events()
        val provider = FileInputStreamProvider(events)
        var results : InputStream? = null
        events.inputLoadedNotification.subscribe {
            results = it
        }

        // When we load the file
        events.fileImportRequest.push(license)

        // Then it should be converted to an input stream
        val result = results?.bufferedReader().use { it?.readText() }
        assertTrue(result != null && result.contains("Apache License"))
    }

    @Test
    fun `should strip the quotes from a file so we can paste it from a Windows path`() {
        // Given a file that we know exists, with quotes
        var license = "\"LICENSE.txt\""

        // And an input stream provider that we're listening to
        val events = Events()
        val provider = FileInputStreamProvider(events)
        var results : InputStream? = null
        events.inputLoadedNotification.subscribe {
            results = it
        }

        // When we load the file
        events.fileImportRequest.push(license)


        // Then it should be converted to an input stream anyway
        val result = results?.bufferedReader().use { it?.readText() }
        assertTrue(result != null && result.contains("Apache License"))
    }

    @Test
    fun shouldTellUsIfTheFileDoesntExist() {
        // Given a file that doesn't exist
        var notThereFile = "IDontExist.txt"

        // And an input stream provider that we're listening to
        // And an input stream provider that we're listening to
        val events = Events()
        val provider = FileInputStreamProvider(events)
        var results : UserNotification? = null
        events.messageNotification.subscribe {
            results = it
        }

        // When we try to load the file
        events.fileImportRequest.push(notThereFile)

        // Then we should be notified that it's not there
        assertEquals("The file \"IDontExist.txt\" could not be found", results?.message)
    }
}