package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.CycleTime
import com.lunivore.montecarluni.model.Distributions
import com.lunivore.montecarluni.model.Record
import com.lunivore.montecarluni.model.WorkItemsClosedInWeek

class DistributionsMarshall(val events : Events,
                            val weeklyDistCreator: (List<Record>) -> List<WorkItemsClosedInWeek>,
                            val cycleTimeCreator: (List<Record>) -> List<CycleTime>){

    constructor(events : Events) : this(
            events,
            WeeklyDistributionCalculator()::calculateDistribution,
            CycleTimesCalculator()::calculateCycleTimes){

    }

    init {
        events.recordsParsedNotification.subscribe {
            if (it.isEmpty()) {
                events.distributionChangeNotification.push(Distributions.EMPTY)
            } else {
                events.distributionChangeNotification.push(
                        Distributions(weeklyDistCreator.invoke(it), cycleTimeCreator.invoke(it)))
            }
        }
        events.clearRequest.subscribe { events.distributionChangeNotification.push(Distributions.EMPTY)}
    }
}
