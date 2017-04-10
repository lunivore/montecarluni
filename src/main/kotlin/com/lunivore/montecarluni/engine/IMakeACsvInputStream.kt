package com.lunivore.montecarluni.engine

import java.io.InputStream

interface IMakeACsvInputStream {
    fun fileAsInputStream(filename: String): InputStream?

}