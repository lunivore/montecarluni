package com.lunivore.montecarluni.engine

import junit.framework.Assert.assertTrue
import org.junit.Test


class FileInputStreamProviderTest() {

    @Test
    fun shouldLoadAFileAndConvertToAnInputStream() {
        // Given a file that we know exists
        var license = "LICENSE.txt"

        // When we load it
        var stream = FileInputStreamProvider().fetch(license)

        // Then it should be converted to an input stream
        var result = stream.bufferedReader().use { it.readText() }
        assertTrue(result.contains("Apache License"))
    }

    @Test
    fun shouldStripTheQuotesFromAFileSoWeCanPasteItFromWindowsPath() {
        // Given a file that we know exists, with quotes
        var license = "\"LICENSE.txt\""

        // When we load it
        var stream = FileInputStreamProvider().fetch(license)

        // Then it should be converted to an input stream anyway
        var result = stream.bufferedReader().use { it.readText() }
        assertTrue(result.contains("Apache License"))
    }
}