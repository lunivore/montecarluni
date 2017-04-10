package com.lunivore.montecarluni.engine

import java.time.LocalDateTime

interface ICalculateWeeklyDistribution {
    fun calculateDistribution(completedDates : List<LocalDateTime>): List<Int>

}