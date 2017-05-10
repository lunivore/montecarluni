package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.model.Record

interface ICalculateWeeklyDistribution {
    fun calculateDistribution(completedDates: List<Record>): List<Int>

}