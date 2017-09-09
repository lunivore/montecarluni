package com.lunivore.montecarluni.steps

import com.lunivore.montecarluni.app.CycleTimePresenter
import com.lunivore.montecarluni.app.WorkItemsDoneInWeekPresenter
import com.lunivore.montecarluni.glue.Scenario
import com.lunivore.montecarluni.glue.World
import com.lunivore.stirry.Stirry
import javafx.scene.control.TabPane
import javafx.scene.control.TableView
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class DistributionSteps(world: World) : Scenario(world) {

    init {
        Then("^I should see the distribution$", 2000, {expectedDistributionAsOneLine : String ->
            val expectedDistribution = expectedDistributionAsOneLine.split(',')
                    .map { it.trim()}
                    .joinToString(separator = "\n")

            val distributionControl = Stirry.findInRoot<TableView<WorkItemsDoneInWeekPresenter>> {
                it.id == "weeklyDistributionOutput"
            }.value

            val distributions = distributionControl.items.map {
                it.rangeAsString + " | " + it.count}
                    .joinToString(separator = "\n")

            assertEquals(expectedDistribution, distributions)
        });

        Then("^the cycle times should contain$", 2000, {expectedFragment : String ->
            val distributionTabs = Stirry.findInRoot<TabPane> { it.id == "distributionTabs" }.value
            val cycleTimesTab = distributionTabs.tabs.first({it.id =="cycleTimesTab"})
            distributionTabs.selectionModel.select(cycleTimesTab)

            val distributionTable = Stirry.findInRoot<TableView<CycleTimePresenter>>{
                it.id == "cycleTimesOutput"}.value

            val cycleTimes = distributionTable.items.map {
                it.id + " | " + it.date + " | " + it.gap
            }.joinToString(separator = "\n")

            assertTrue(cycleTimes.contains(expectedFragment))

        })
    }
}

