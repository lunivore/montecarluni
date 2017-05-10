package com.lunivore.montecarluni.engine

import java.io.InputStream

interface IProvideInputStreams {
    fun fetch(filename: String): InputStream

}