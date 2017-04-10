package com.lunivore.montecarluni.engine

import java.io.InputStream
import java.time.LocalDateTime

interface IParseCsvStreams {
    fun parseCompletedDates(stream: InputStream?): List<LocalDateTime>

}