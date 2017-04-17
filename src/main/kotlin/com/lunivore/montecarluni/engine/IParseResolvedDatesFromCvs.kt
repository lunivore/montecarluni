package com.lunivore.montecarluni.engine

import java.io.InputStream
import java.time.LocalDateTime

interface IParseResolvedDatesFromCvs {
    fun parseResolvedDates(stream: InputStream): List<LocalDateTime>

}