package com.lunivore.montecarluni.model

import java.time.LocalDateTime

data class Record(private val resolvedDate: LocalDateTime?, private val lastUpdatedDate: LocalDateTime?) {

    val resolvedOrLastUpdatedDate: LocalDateTime
        public get() {
            if (resolvedDate == null) {
                if(lastUpdatedDate == null) {
                    throw IllegalStateException(
                            "Attempted to use a record with neither resolved nor updated date -" +
                                    "this should never happen!")
                } else return lastUpdatedDate
            } else return resolvedDate
        }
}