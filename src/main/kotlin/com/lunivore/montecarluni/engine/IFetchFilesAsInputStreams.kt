package com.lunivore.montecarluni.engine

import java.io.InputStream

interface IFetchFilesAsInputStreams {
    fun fetch(filename: String): InputStream

}