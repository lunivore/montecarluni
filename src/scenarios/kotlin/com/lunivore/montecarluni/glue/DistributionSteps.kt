package com.lunivore.montecarluni.glue

import com.lunivore.stirry.Stirry
import cucumber.api.java8.En
import javafx.scene.control.Label
import org.junit.Assert.assertEquals

class DistributionSteps(world: World) : En {

    init {
        Then("^I should see the distribution$", {expectedDistributionAsOneLine : String ->
            val expectedDistribution = expectedDistributionAsOneLine.split(',')
                    .map { it.trim()}
                    .joinToString(separator = "\n")

            val distributionControl = Stirry.find<Label> {it.id == "distributionOutput"}
            val distributions = distributionControl?.text

            assertEquals(expectedDistribution, distributions)
        });
    }
}