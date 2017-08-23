package com.lunivore.montecarluni.steps

import com.lunivore.montecarluni.glue.Scenario
import com.lunivore.montecarluni.glue.World
import com.lunivore.stirry.Stirry.Companion.findInRoot
import com.lunivore.stirry.Stirry.Companion.findRoot
import com.lunivore.stirry.Stirry.Companion.runOnPlatform
import com.lunivore.stirry.fireAndStir
import com.lunivore.stirry.setTextAndStir
import javafx.scene.control.Button
import javafx.scene.control.TabPane
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import org.junit.Assert.assertEquals

class ForecastSteps(val world: World) : Scenario(world) {

    init {
        When("^I ask for a forecast for the next (\\d+) stories$", { number: Int ->
            val tabs = findRoot() as TabPane
            runOnPlatform { tabs.selectionModel.select(tabs.tabs.first{it.id == "forecastTab"}) }

            findInRoot<TextField>({it.id == "numStoriesForecastInput"}).value.setTextAndStir("100")
            findInRoot<Button> ({it.id == "forecastButton" }).value.fireAndStir()
        })

        Then("^I should see a percentage forecast$", { expectedForecast: String ->
            val forecast = findInRoot<TableView<Map<String, String>>>({it.id == "forecastOutput"}).value

            val forecastAsString = forecast.items.map { "${it["probability"]} | ${it["forecastDate"]}" }
                    .joinToString(separator = "\n")

            assertEquals(expectedForecast, forecastAsString)
        })
    }
}