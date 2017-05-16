package com.lunivore.montecarluni.engine

import java.io.FileInputStream
import java.io.InputStream

class FileInputStreamProvider : IProvideInputStreams {

    override fun fetch(filename: String): InputStream {
        var candidate = trimQuotes(filename)
        return FileInputStream(candidate)
    }

    private fun trimQuotes(filename: String): String {
        var candidate = filename
        if (candidate.startsWith('"') && candidate.endsWith('"')) {
            candidate = filename.trim('"')
        }
        return candidate
    }
}
