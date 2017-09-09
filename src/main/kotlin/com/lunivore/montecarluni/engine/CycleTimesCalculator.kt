package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.CycleTime
import com.lunivore.montecarluni.model.Record
import java.time.Duration

class CycleTimesCalculator() {

    fun calculateCycleTimes(records: List<Record>): List<CycleTime> {
        val sortedRecords = records.sortedBy { it.resolvedOrLastUpdatedDate }
        return sortedRecords.mapIndexed { i, r ->
            if (i == 0) {
                CycleTime("", r.resolvedOrLastUpdatedDate, null)
            } else {
                CycleTime("", r.resolvedOrLastUpdatedDate,
                        Duration.between(sortedRecords[i-1].resolvedOrLastUpdatedDate, r.resolvedOrLastUpdatedDate))
            }}
    }

}