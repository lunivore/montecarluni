package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.Record
import java.io.InputStream

interface ICreateRecordsFromCvs {
    fun parseResolvedDates(stream: InputStream): List<Record>

}