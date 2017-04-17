package com.lunivore.montecarluni.engine

import java.io.FileInputStream
import java.io.InputStream

class ResourceOrFilepathFetcher : IFetchFilesAsInputStreams {

    override fun fetch(filename: String): InputStream {
        var stream = this.javaClass.getResourceAsStream(filename)

        if (stream == null) {
            stream = FileInputStream(filename)
        }

        if (stream == null) {
            throw Exception("Could not load file at ${filename}")
        }
        return stream
    }
}

