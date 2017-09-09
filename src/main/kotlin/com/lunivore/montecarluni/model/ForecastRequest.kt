package com.lunivore.montecarluni.model

import java.time.LocalDate

data class ForecastRequest(
        val numWorkItems: Int,
        val startDate : LocalDate?,
        val useSelection : Boolean = false,
        val selectedIndices : List<Int> = listOf())