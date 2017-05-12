package com.lunivore.montecarluni.model

import java.time.LocalDateTime

data class Record(val resolvedDate: LocalDateTime?, val lastUpdatedDate: LocalDateTime?) {

    fun getResolvedOrLastUpdatedDate() : LocalDateTime? {
        return resolvedDate?:lastUpdatedDate
    }
}