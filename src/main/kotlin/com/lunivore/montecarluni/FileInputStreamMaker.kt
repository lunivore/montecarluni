package com.lunivore.montecarluni

import java.io.FileInputStream
import java.io.InputStream

class FileInputStreamMaker {
    fun fileAsInputStream(filename: String): InputStream? {
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